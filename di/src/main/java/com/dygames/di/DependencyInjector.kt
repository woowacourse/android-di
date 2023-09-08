package com.dygames.di

import com.dygames.di.annotation.Injectable
import com.dygames.di.annotation.Qualifier
import com.dygames.di.error.InjectError
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
        return findDependency(type, qualifier) ?: instantiate(type).apply {
            injectFields(this)
        }
    }

    fun instantiate(type: KType): Any {
        val constructor = type.jvmErasure.primaryConstructor
            ?: throw InjectError.ConstructorNoneAvailable(type)
        val parameters = constructor.parameters
        val arguments = gatherArguments(parameters)
        return constructor.call(*arguments)
    }

    private fun findDependency(type: KType, qualifier: Annotation?): Any? {
        if (!::dependencies.isInitialized) throw InjectError.DependenciesNotInitialized()
        return dependencies.findDependency(type, qualifier)
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
            .forEach {
                val qualifier = findQualifier(it.annotations)
                it.setter.call(instance, inject(it.returnType, qualifier))
            }
        return instance
    }

    private fun findQualifier(annotations: List<Annotation>): Annotation? {
        return annotations.firstOrNull {
            it.annotationClass.annotations.filterIsInstance<Qualifier>().isNotEmpty()
        }
    }
}
