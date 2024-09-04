package woowacourse.shopping.data

class DefaultAppContainer : AppContainer() {
    override val cartRepository: CartRepository by lazy {
        DefaultCartRepository()
    }
    override val productRepository: ProductRepository by lazy {
        DefaultProductRepository()
    }
}
