package woowacourse.shopping.common

import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class DefaultAppContainer : AppContainer {
    private val defaultProductRepository: ProductRepository by lazy { DefaultProductRepository() }
    private val defaultCartRepository: CartRepository by lazy { DefaultCartRepository() }
}
