package woowacourse.util

import android.content.Context
import com.example.di.module.ActivityModule
import com.example.di.module.ApplicationModule
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class FakeApplicationModule(context: Context) : ApplicationModule(context) {
    private val cartRepository = getFakeCartRepository()
    fun getCartRepository(): CartRepository = cartRepository
}

fun getFakeApplicationModule(context: Context): ApplicationModule = FakeApplicationModule(context)

fun getFakeActivityModule(
    context: Context,
    fakeApplicationModule: ApplicationModule,
): ActivityModule =
    object : ActivityModule(context, fakeApplicationModule) {
        fun getProductRepository(): ProductRepository {
            return getFakeProductRepository()
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

fun getFakeProductRepository(): ProductRepository = object : ProductRepository {
    private val products: List<Product> = getProducts()

    override fun getAllProducts(): List<Product> {
        return products
    }
}

fun getProducts(names: List<String> = listOf("사과", "포도")): List<Product> {
    return mutableListOf<Product>().apply {
        names.forEach {
            add(getProduct(it))
        }
    }
}

fun getProduct(
    name: String,
    price: Int = 10_000,
    imageUrl: String = "https://cdn-mart.baemin.com/sellergoods/api/main/711c39ee-ff8e-4983-aa01-f669e82bddae.jpg?h=700&w=700",
): Product {
    return Product(name, price, imageUrl)
}
