package woowacourse.shopping.common

import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class DefaultAppContainer private constructor() : AppContainer {
    private val defaultProductRepository: ProductRepository by lazy { DefaultProductRepository() }
    private val defaultCartRepository: CartRepository by lazy { DefaultCartRepository() }

    companion object {
        private var DEFAULT_APP_CONTAINER: DefaultAppContainer? = null

        fun create(): DefaultAppContainer {
            if (DEFAULT_APP_CONTAINER == null) DEFAULT_APP_CONTAINER = DefaultAppContainer()
            return DEFAULT_APP_CONTAINER!!
        }
    }
}
