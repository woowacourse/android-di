package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class ViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.constructors.first()
        val params =
            constructor.parameters
                .map { param ->
                    when (param.type.kotlin) {
                        CartRepository::class -> AppContainer.cartRepository
                        ProductRepository::class -> AppContainer.productRepository
                        else -> throw IllegalArgumentException("${param.type} 타입에 대한 provider가 없습니다")
                    }
                }.toTypedArray()

        return constructor.newInstance(*params) as T
    }
}
