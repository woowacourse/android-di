package woowacourse.shopping.data.repositoryImpl

import com.harodi.Qualifier
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = dao.getAll().map { it.toDomain() }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}

@Qualifier
@Target(AnnotationTarget.PROPERTY)
annotation class DefaultCart
