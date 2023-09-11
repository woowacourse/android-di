package woowacourse.util

import android.content.Context
import com.example.di.module.ApplicationModule
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.dataSorce.InMemoryLocalDataSource
import woowacourse.shopping.data.dataSorce.LocalDataSource
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.di.annotation.RoomDb
import woowacourse.shopping.di.annotation.RoomDbCartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class FakeApplicationModule(context: Context) : ApplicationModule(context) {
    private val cartRepository = getFakeCartRepository()
    fun getCartRepository(): CartRepository = cartRepository

    fun getRoomCartRepository(): CartRepository {
        return DefaultCartRepository(InMemoryLocalDataSource())
    }

    @RoomDbCartRepository
    fun getRoomCartRepository(@RoomDb localDataSource: LocalDataSource): CartRepository {
        return DefaultCartRepository(localDataSource)
    }

    @RoomDb
    fun getRoomDataSource(): LocalDataSource {
        return InMemoryLocalDataSource()
    }
}

fun getFakeCartRepository(): CartRepository = object : CartRepository {
    private val carts = mutableListOf<CartProduct>()

    override suspend fun addCartProduct(product: Product) {
        carts.add(product.toEntity().toDomain())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return carts.toList()
    }

    override suspend fun deleteCartProduct(id: Long) {
        carts.removeAt(id.toInt())
    }
}

fun getProducts(names: List<String> = listOf("사과", "포도")): List<Product> {
    return names.map { getProduct(it) }
}

fun getProduct(
    name: String,
    price: Int = 10_000,
    imageUrl: String = "https://cdn-mart.baemin.com/sellergoods/api/main/711c39ee-ff8e-4983-aa01-f669e82bddae.jpg?h=700&w=700",
): Product {
    return Product(name, price, imageUrl)
}

fun getCartProducts(names: List<String> = listOf("사과", "포도")): List<CartProduct> {
    return names.mapIndexed { index, name -> getCartProduct(index.toLong(), name) }
}

fun getCartProduct(
    id: Long,
    name: String,
    createdAt: Long = 1L,
): CartProduct {
    return CartProduct(id, createdAt, getProduct(name))
}
