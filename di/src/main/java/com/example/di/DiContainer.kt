package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DiContainer {
    private data class Key(val type: KClass<*>, val qualifier: KClass<out Annotation>?)

    private val providers = mutableMapOf<Key, Lazy<Any>>()
    private val bindings = mutableMapOf<Key, KClass<*>>()

    fun <T : Any> addProviders(
        kClazz: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        provider: () -> T,
    ) {
        val key = Key(kClazz, qualifier)
        providers[key] = lazy(LazyThreadSafetyMode.SYNCHRONIZED) { provider() }
    }

    fun <I : Any> bind(
        from: KClass<I>,
        to: KClass<out I>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        require(from != to)
        require(from.java.isAssignableFrom(to.java))
        bindings[Key(from, qualifier)] = to
    }

    fun <T : Any> getProvider(
        kClazz: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val key = Key(kClazz, qualifier)
        providers[key]?.let {
            return it.value as T
        }
        val concreteClazz = bindings[key] ?: kClazz

        if (concreteClazz.java.isInterface) {
            throw IllegalArgumentException()
        }

        val lazyProvider =
            providers.getOrPut(Key(concreteClazz, qualifier)) {
                lazy(LazyThreadSafetyMode.SYNCHRONIZED) { createInstance(concreteClazz) }
            }

        if (kClazz != concreteClazz) {
            providers[Key(concreteClazz, qualifier)] = lazyProvider
        }

        return lazyProvider.value as T
    }

    private fun <T : Any> createInstance(kClazz: KClass<T>): T {
        val constructor =
            kClazz.primaryConstructor
                ?: throw IllegalArgumentException()
        val arguments =
            constructor.parameters.associateWith { parameter ->
                val parameterClass = parameter.type.classifier as KClass<*>
                val qualifier =
                    parameter.annotations.firstOrNull { annotation ->
                        annotation.annotationClass.annotations.any { it is Qualifier }
                    }?.annotationClass
                getProvider(parameterClass, qualifier)
            }
        val instance = constructor.callBy(arguments)
        Injector.inject(instance)
        return instance
    }
}
