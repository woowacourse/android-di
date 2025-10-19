package woowacourse.shopping.data

import com.example.di.annotation.Inject
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.Product
import woowacourse.shopping.model.repository.CartRepository

class DefaultCartRepository
    @Inject
    constructor(
        private val dao: CartProductDao,
    ) : CartRepository {
        init {
            println("[DI] CartRepository created (AppScoped)")
        }

        override suspend fun addCartProduct(product: Product) {
            dao.insert(product.toEntity())
        }

        override suspend fun getAllCartProducts(): List<Product> = dao.getAll().map { it.toDomain() }

        override suspend fun deleteCartProduct(id: Long) {
            dao.delete(id)
        }
    }
