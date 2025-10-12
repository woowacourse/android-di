package woowacourse.shopping.data

import com.example.di.MyInjector
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.model.Product

class DatabaseCartRepository
    @MyInjector
    constructor(
        @MyInjector private val cartDao: CartProductDao,
    ) : CartRepository {
        override suspend fun addCartProduct(product: Product) = cartDao.insert(product.toEntity())

        override suspend fun getAllCartProducts(): List<Product> = cartDao.getAll().map { it.toDomain() }

        override suspend fun deleteCartProduct(id: Long) = cartDao.delete(id)
    }
