package woowacourse.shopping.data.repository

import com.example.bbottodi.di.annotation.InDisk
import com.example.bbottodi.di.annotation.Inject
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository

@InDisk
class InDiskCartRepository(
    @Inject
    private val cartProductDao: CartProductDao,
) : CartRepository {

    override suspend fun addCartProduct(product: Product) {
        cartProductDao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> {
        return cartProductDao.getAll().map { it.toProduct() }
    }

    override suspend fun deleteCartProduct(id: Int) {
        cartProductDao.delete(id.toLong())
    }
}
