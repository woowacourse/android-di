package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.ShoppingApplication
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

object DependencyInjector {

    inline fun <reified T : Any> inject(): T {
        val primaryConstructor = requireNotNull(T::class.primaryConstructor)
        val arguments = primaryConstructor.parameters.instantiate()

        return primaryConstructor.call(*arguments)
    }

    fun List<KParameter>.instantiate(): Array<Any?> =
        map { (it.type.classifier as KClass<*>).instantiateRecursively() }.toTypedArray()

    private fun KClass<*>.instantiateRecursively(): Any {
        val constructor = primaryConstructor ?: run {
            return requireNotNull(ShoppingApplication.container.find(this))
        }

        if (constructor.parameters.isEmpty()) {
            return ShoppingApplication.container.find(this) ?: constructor.call()
        }
        val arguments = constructor.parameters.map {
            (it.type.classifier as KClass<*>).instantiateRecursively()
        }

        return constructor.call(*arguments.toTypedArray())
    }

    inline fun <reified T : ViewModel> getInjectedViewModelFactory(): ViewModelProvider.Factory =
        viewModelFactory {
            initializer {
                inject<T>()
            }
        }
}
