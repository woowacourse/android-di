package woowacourse.shopping.di

import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object DependencyInjector {
    private val instances = mutableMapOf<KClass<*>, Any>()
    private val creating = mutableSetOf<KClass<*>>()

    fun <T : Any> setInstance(
        kClass: KClass<T>,
        instance: T,
    ) {
        instances[kClass] = instance
    }

    fun <T : Any> getInstance(
        kClass: KClass<T>,
        savedStateHandle: SavedStateHandle? = null,
    ): T {
        return instances[kClass] as? T ?: run {
            val instance = createInstance(kClass, savedStateHandle)
            instances[kClass] = instance
            return (instances[kClass] as T).also { instances.remove(kClass) }
        }
    }

    fun <T : Any> injectAnnotatedProperties(
        kClass: KClass<T>,
        instance: T,
    ) {
        kClass.members
            .filterIsInstance<KMutableProperty1<Any, Any>>()
            .filter { property -> property.findAnnotation<RequireInjection>() != null }
            .forEach { property ->
                property.isAccessible = true
                val dependencyClass = property.returnType.classifier as? KClass<*>
                dependencyClass?.let {
                    property.setter.call(
                        instance,
                        getInstance(it),
                    )
                }
            }
    }

    private fun createInstance(
        kClass: KClass<*>,
        handle: SavedStateHandle?,
    ): Any {
        if (creating.contains(kClass)) throw IllegalStateException("순환 참조")
        creating.add(kClass)

        try {
            val primaryConstructor = kClass.primaryConstructor

            primaryConstructor?.let {
                val params =
                    it.parameters.associateWith { param ->
                        when (param.type.classifier) {
                            SavedStateHandle::class -> requireNotNull(handle) { "SavedStateHandle 필요" }
                            else -> getInstance(param.type.classifier as KClass<*>)
                        }
                    }
                return it.callBy(params)
            }

            kClass.objectInstance?.let { obj -> return obj }

            return kClass.java.getDeclaredConstructor().newInstance()
        } finally {
            creating.remove(kClass)
            instances.remove(kClass)
        }
    }
}
