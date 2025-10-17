package woowacouse.shopping.di

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.savedstate.SavedStateRegistryOwner
import woowacouse.shopping.di.annotation.Inject
import woowacouse.shopping.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class DiFactory(
    private val container: Container,
    private val context: Context,
) {
    fun <T : Any> create(
        key: String,
        modelClass: KClass<T>,
        handle: SavedStateHandle? = null,
    ): T {
        val constructor =
            modelClass.primaryConstructor
                ?: throw IllegalArgumentException("No primary constructor found for $modelClass")

        val parameters =
            constructor.parameters
                .map { parameter ->
                    val kParameter =
                        parameter.type.classifier as? KClass<*>
                            ?: throw IllegalArgumentException("No matching dependency for ${parameter.name}")

                    when (kParameter) {
                        Context::class -> context
                        SavedStateHandle::class -> handle
                        else ->
                            container.get(
                                kParameter,
                                parameter.findAnnotation<Qualifier>()?.value,
                            )
                    }
                }.toTypedArray()

        constructor.isAccessible = true
        val instance = constructor.call(*parameters)

        modelClass.memberProperties
            .filter { it.findAnnotation<Inject>() != null }
            .forEach { property ->
                val mutable =
                    (property as? KMutableProperty1<Any, Any?>)
                        ?: error("@Inject target must be a 'var' mutable property: ${property.name} in $modelClass")

                val targetType =
                    property.returnType.classifier as? KClass<*>
                        ?: error("@Inject target must be a 'var' mutable property: ${property.name} in $modelClass")

                val qualifier = property.findAnnotation<Qualifier>()?.value
                val dependency =
                    when (targetType) {
                        Context::class -> context
                        SavedStateHandle::class -> handle
                        else -> {
                            container.get(targetType, qualifier)
                                ?: throw IllegalArgumentException("No matching dependency for ${targetType.simpleName} in $modelClass")
                        }
                    }

                mutable.isAccessible = true
                mutable.setter.isAccessible = true
                mutable.setter.call(instance, dependency)
            }
        return instance
    }

    fun inject(target: Any) {
        val kClass = target::class
        kClass.memberProperties
            .filter { it.findAnnotation<Inject>() != null }
            .forEach { property ->
                val mutable =
                    property as? KMutableProperty1<Any, Any?>
                        ?: error("@Inject target must be var: ${property.name}")

                val targetType =
                    property.returnType.classifier as? KClass<*>
                        ?: error("@Inject target must be a valid class: ${property.name}")

                val qualifier = property.findAnnotation<Qualifier>()?.value
                val dependency =
                    container.get(targetType, qualifier)
                        ?: create(property.name, targetType)

                mutable.isAccessible = true
                mutable.setter.call(target, dependency)
            }
    }
}
