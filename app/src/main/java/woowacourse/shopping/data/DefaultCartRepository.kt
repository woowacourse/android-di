package woowacourse.shopping.data

import woowacourse.shopping.model.Product

// TODO: Step2 - CartProductDao를 참조하도록 변경
class DefaultCartRepository(private val cartLocalDataSource: CartLocalDataSource) : CartRepository {

    override fun addCartProduct(product: Product) {
        cartLocalDataSource.addCartProduct(product)
    }

    override fun getAllCartProducts(): List<Product> {
        return cartLocalDataSource.cartProducts
    }

    override fun deleteCartProduct(id: Int) {
        cartLocalDataSource.deleteCartProduct(id)
    }
}
