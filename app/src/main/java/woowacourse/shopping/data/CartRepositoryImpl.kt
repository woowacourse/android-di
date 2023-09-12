package woowacourse.shopping.data

import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

// TODO: Step2 - CartProductDao를 참조하도록 변경
class CartRepositoryImpl @Inject constructor(private val cartProduct: CartProductDao) :
    CartRepository {
    override suspend fun addCartProduct(product: Product) {
        cartProduct.insert(CartProductEntity(product.name, product.price, product.imageUrl))
    }

    override suspend fun getAllCartProducts(): List<Product> {
        return cartProduct.getAll().map { Product(it.name, it.price, it.imageUrl, it.createdAt) }
    }

    override suspend fun deleteCartProduct(id: Int) {
        cartProduct.delete(id.toLong())
    }
}
