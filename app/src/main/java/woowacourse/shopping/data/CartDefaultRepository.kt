package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

// TODO: Step2 - CartProductDao를 참조하도록 변경
class CartDefaultRepository(
    private val cartProductDao: CartProductDao,
) : CartRepository {

    override suspend fun addCartProduct(product: Product) {
        cartProductDao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> {
        val cartProductEntities = cartProductDao.getAll()
        return cartProductEntities.map { it.toProduct() }
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProductDao.delete(id)
    }
}
