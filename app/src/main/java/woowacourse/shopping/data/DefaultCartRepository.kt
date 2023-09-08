package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toModel
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.Product

// TODO: Step2 - CartProductDao를 참조하도록 변경
class DefaultCartRepository(
    private val cartProductDao: CartProductDao
) : CartRepository {

    private val cartProducts: MutableList<Product> = mutableListOf()

    override suspend fun addCartProduct(product: Product) {
//        cartProducts.add(product)
        cartProductDao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
//        return cartProducts.toList()
        return cartProductDao.getAll().map { it.toModel() }
    }

    override fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}
