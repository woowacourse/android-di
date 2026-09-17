package woowacourse.shopping.data

class AppContainer {
    private val productRepository: ProductRepository = ProductRepository()
    private val cartRepository: CartRepository = CartRepository()
}
