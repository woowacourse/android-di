package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

class AutoDIViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val constructor = modelClass.kotlin.constructors.first()
        val args =
            constructor.parameters
                .map { param ->
                    when (param.type.classifier) {
                        ProductRepository::class -> RepositoryModule.productRepository
                        CartRepository::class -> RepositoryModule.cartRepository
                        else -> throw IllegalArgumentException("주입 불가: ${param.type}")
                    }
                }.toTypedArray()

        return constructor.call(*args)
    }
}
