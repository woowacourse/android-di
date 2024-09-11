package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toModel
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.model.Product

// TODO: Step2 - CartProductDao를 참조하도록 변경
class DefaultCartRepository(
    private val cartProductDao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        cartProductDao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> = cartProductDao.getAll().map { it.toModel() }

    override suspend fun deleteCartProduct(id: Int) {
        cartProductDao.delete(id)
    }
}
