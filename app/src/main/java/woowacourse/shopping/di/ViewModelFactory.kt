package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.reflect.full.primaryConstructor

object ViewModelFactory {

    fun <T : Any> getViewModelFactory(vm: KClass<T>): ViewModelProvider.Factory {
        val parameters = vm.primaryConstructor?.parameters
        val repositoryTypes = mutableListOf<Any>()

        if (parameters != null) {
            for (param in parameters) {
                when (param.type) {
                    ProductRepository::class.createType() -> repositoryTypes.add(
                        ProductRepositoryImpl(),
                    )
                    CartRepository::class.createType() -> repositoryTypes.add(CartRepositoryImpl())
                }
            }
        }
        return createViewModelFactory(vm, repositoryTypes)
    }

    private fun <T : Any> createViewModelFactory(
        vm: KClass<T>,
        repositoryTypes: List<Any>,
    ): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val constructor = vm.java.constructors.firstOrNull()
                if (constructor != null) {
                    val viewModelInstance =
                        constructor.newInstance(*repositoryTypes.toTypedArray())
                    return viewModelInstance as T
                }
                throw IllegalArgumentException()
            }
        }
    }
}
