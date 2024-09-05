package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class CommonViewModelFactory(
    application: ShoppingApplication,
) : ViewModelProvider.Factory {
    private val dependencyMap: Map<KClass<*>, Any> =
        mapOf(
            ProductRepository::class to application.container.productRepository,
            CartRepository::class to application.container.cartRepository,
        )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("ViewModel must have a primary constructor: ${kClass.simpleName}")

        val args =
            constructor.parameters.map { parameter ->
                val dependency = dependencyMap[parameter.type.classifier as KClass<*>]
                dependency ?: throw IllegalArgumentException("Unresolved dependency for ${parameter.type}")
            }.toTypedArray()

        return constructor.call(*args)
    }
}
