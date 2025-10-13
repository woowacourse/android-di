package woowacourse.shopping.data.repository

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toPresentation
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.Product
import woowacourse.shopping.ui.model.CartUiModel

class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) = dao.insert(product.toEntity())

    override suspend fun getAllCartProducts(): List<CartUiModel> = dao.getAll().map { it.toPresentation() }

    override suspend fun deleteCartProduct(id: Long) = dao.delete(id)
}
