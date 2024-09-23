package org.library.haeum

import org.library.haeum.di.HaeumInject
import org.library.haeum.di.Module
import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

class Container(
    private val module: Module,
) {
    fun <T : Any> injectTo(kClass: KClass<out T>): T {
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("${kClass}에 주 생성자가 없습니다.")
        val arguments =
            constructor.valueParameters.associateWith {
                resolveDependency(it)
            }
        val instance = constructor.callBy(arguments)

        kClass.declaredMemberProperties.filterIsInstance<KMutableProperty1<T, *>>()
            .filter { it.hasAnnotation<HaeumInject>() }
            .forEach {
                it.setter.call(instance, resolveDependency(it))
            }
        return instance
    }

    fun resolveDependency(property: KProperty1<*, *>): Any {
        instances[property.returnType::class]?.let {
            return instances
        }

        val qualifier =
            property.annotations.find { it.annotationClass.hasAnnotation<Qualifier>() }
        if (qualifier != null) {
            val qualifiedInstance = instances[qualifiedInstances[qualifier]]
            if (qualifiedInstance != null) return qualifiedInstance
            return resolveDependency(qualifier)
        }
        return resolveDependency(property.returnType)
    }

    private fun resolveDependency(qualifier: Annotation): Any {
        val function =
            module::class.declaredMemberFunctions.find { func ->
                func.annotations.any { it == qualifier }
            } ?: throw IllegalArgumentException("$qualifier 가 없습니다.")

        return invokeProvider(function)
    }

    private fun resolveDependency(parameter: KParameter): Any {
        instances[parameter.type::class]?.let {
            return instances
        }
        parameter.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }?.let {
            val qualifiedInstance = instances[qualifiedInstances[it]]
            if (qualifiedInstance != null) return qualifiedInstance
            return resolveDependency(it)
        }
        return resolveDependency(parameter.type)
    }

    private fun resolveDependency(type: KType): Any {
        val functions =
            module::class.declaredMemberFunctions.filter { func ->
                func.returnType == type
            }
        return invokeProvider(functions.first())
    }

    fun invokeProvider(provider: KFunction<*>): Any {
        var instance = instances[provider.returnType.jvmErasure]
        if (instance != null) {
            return instance
        }
        val arguments = arrayListOf<Any>()
        provider.valueParameters.forEach { parameter ->
            arguments.add(resolveDependency(parameter))
        }

        instance =
            provider.call(module, *arguments.toTypedArray())
                ?: throw IllegalArgumentException("의존성 주입에 실패했습니다.")

        val qualifier =
            provider.annotations.find { annotation ->
                annotation.annotationClass.annotations.any { sub ->
                    sub.annotationClass == Qualifier::class
                }
            }
        if (qualifier != null) {
            qualifiedInstances[qualifier] = instance::class
        }
        instances[instance::class] = instance
        return instance
    }

    companion object {
        private val instances = hashMapOf<KClass<out Any>, Any>()
        private val qualifiedInstances = hashMapOf<Annotation, KClass<out Any>>()
    }
}
