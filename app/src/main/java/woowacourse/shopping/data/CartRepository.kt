package woowacourse.shopping.data

import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.di.DependencyContainer
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

@DependencyContainer.Qualifier
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class RoomCart

@DependencyContainer.Qualifier
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class InMemoryCart

interface CartRepository {
    suspend fun addCartProduct(product: Product)

    suspend fun getAllCartProducts(): List<CartProduct>

    suspend fun deleteCartProduct(id: Long)
}

class RoomCartRepository(
    private val cartProductDao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        cartProductDao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = cartProductDao.getAll().map { it.toDomain() }

    override suspend fun deleteCartProduct(id: Long) {
        cartProductDao.delete(id)
    }
}

class InMemoryCartRepository : CartRepository {
    private val cartProducts: MutableMap<Long, CartProduct> = mutableMapOf()
    private var nextId: Long = 1L

    override suspend fun addCartProduct(product: Product) {
        val cartProduct =
            CartProduct(
                id = nextId++,
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                createdAt = System.currentTimeMillis(),
            )

        cartProducts[cartProduct.id] = cartProduct
    }

    override suspend fun getAllCartProducts(): List<CartProduct> = cartProducts.values.toList()

    override suspend fun deleteCartProduct(id: Long) {
        cartProducts.remove(id)
    }
}
