package woowacouse.shopping.di

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import woowacouse.shopping.di.annotation.Inject
import woowacouse.shopping.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class ViewModelFactory(
    private val container: Container,
    private val owner: SavedStateRegistryOwner,
    private val defaultArgs: Bundle? = null,
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        val kClass = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("No primary constructor found for $modelClass")

        val parameters =
            constructor.parameters
                .map { parameter ->
                    val kParameter =
                        parameter.type.classifier as? KClass<*>
                            ?: throw IllegalArgumentException("No matching dependency for ${parameter.name}")

                    when (kParameter) {
                        SavedStateHandle::class -> handle
                        else ->
                            container.get(
                                kParameter,
                                parameter.findAnnotation<Qualifier>()?.value,
                            )
                    }
                }.toTypedArray()

        constructor.isAccessible = true
        val viewModel = constructor.call(*parameters)

        kClass.memberProperties
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
                        SavedStateHandle::class -> handle
                        else -> {
                            container.get(targetType, qualifier)
                                ?: throw IllegalArgumentException("No matching dependency for ${targetType.simpleName} in $modelClass")
                        }
                    }

                mutable.isAccessible = true
                mutable.setter.isAccessible = true
                mutable.setter.call(viewModel, dependency)
            }
        return viewModel
    }
}
