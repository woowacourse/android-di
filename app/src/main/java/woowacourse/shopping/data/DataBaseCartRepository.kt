package woowacourse.shopping.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

// TODO: Step2 - CartProductDao를 참조하도록 변경
class DataBaseCartRepository(
    private val cartProductDao: CartProductDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) = withContext(dispatcher) {
        cartProductDao.insert(product.toEntity())
    }


    override suspend fun deleteCartProduct(id: Long) = withContext(dispatcher) {
        cartProductDao.delete(id)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = withContext(dispatcher) {
        cartProductDao.getAll().map { it.toCartProduct() }
    }
}
