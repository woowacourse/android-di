package woowacourse.shopping.data

abstract class AppContainer {
    abstract val cartRepository: CartRepository

    abstract val productRepository: ProductRepository
}
