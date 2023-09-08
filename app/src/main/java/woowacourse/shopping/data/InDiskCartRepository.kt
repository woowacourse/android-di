package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toCartProduct
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.di.annotation.InDisk
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

// TODO: Step2 - CartProductDao를 참조하도록 변경
@InDisk
class InDiskCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return dao.getAll().map { it.toCartProduct() }
    }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
