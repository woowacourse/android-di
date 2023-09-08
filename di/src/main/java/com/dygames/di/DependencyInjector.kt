package com.dygames.di

import com.dygames.di.annotation.Injectable
import com.dygames.di.annotation.Qualifier
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

object DependencyInjector {
    lateinit var dependencies: Dependencies

    inline fun <reified T : Any> inject(): T {
        return inject(typeOf<T>()) as T
    }

    fun inject(type: KType, qualifier: Annotation? = null): Any {
        return findSingleton(type, qualifier) ?: instantiate(type).apply {
            injectFields(this)
        }
    }

    private fun findSingleton(type: KType, qualifier: Annotation?): Any? {
        if (!DependencyInjector::dependencies.isInitialized) throw IllegalStateException("의존이 초기화되지 않았습니다.")
        return dependencies.qualifiers[qualifier]?.let {
            it.constructors[type]?.let { constructor ->
                instantiate(constructor)
            } ?: it.providers[type]?.let { provider ->
                provider()
            }
        }
    }

    private fun instantiate(type: KType): Any {
        val constructor = type.jvmErasure.primaryConstructor
            ?: throw IllegalArgumentException("$type 클래스의 주 생성자가 존재하지 않습니다.")
        val parameters = constructor.parameters
        val arguments = gatherArguments(parameters)
        return constructor.call(*arguments)
    }

    private fun gatherArguments(parameters: List<KParameter>): Array<*> {
        return parameters.map { parameter ->
            val qualifier = findQualifier(parameter.annotations)
            inject(parameter.type, qualifier)
        }.toTypedArray()
    }

    private fun injectFields(instance: Any): Any {
        val fields = instance::class.declaredMemberProperties
        fields.filter { it.annotations.filterIsInstance<Injectable>().isNotEmpty() }
            .filterIsInstance<KMutableProperty<*>>()
            .forEach { it.setter.call(instance, inject(it.returnType)) }
        return instance
    }

    private fun findQualifier(annotations: List<Annotation>): Annotation? {
        return annotations.firstOrNull {
            it.annotationClass.annotations.filterIsInstance<Qualifier>().isNotEmpty()
        }
    }
}
