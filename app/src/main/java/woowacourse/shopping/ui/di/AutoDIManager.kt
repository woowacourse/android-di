package woowacourse.shopping.ui.di

import androidx.lifecycle.ViewModel
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object AutoDIManager {
    private val repositories: MutableMap<String, Any> = mutableMapOf()

    init {
        registerRepository(ProductRepositoryImpl)
        registerRepository(CartRepositoryImpl)
    }

    private fun registerRepository(repository: Any) {
        repositories[repository::class.typeParameters.toString()] = repository
    }

    private fun <T : Any> createInstanceWithParameters(
        clazz: KClass<T>,
        argInputs: Map<String, Any?>
    ): T? {
        val constructor = clazz.primaryConstructor ?: return null

        val args = constructor.parameters.associateWith {
            args -> argInputs[args.name]
        }
        constructor.isAccessible = true
        return constructor.callBy(args)
    }

    fun <T : ViewModel> createViewModelFactory(viewModelClass: KClass<T>): GenericViewModelFactory<ViewModel> {
        val args = mapOf(
            "productRepository" to ProductRepositoryImpl,
            "cartRepository" to CartRepositoryImpl,
        )

        return GenericViewModelFactory(viewModelClass) {
            createInstanceWithParameters(viewModelClass, args) ?: error("")
        }
    }
}
