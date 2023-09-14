package woowacourse.shopping.data

import com.app.covi_di.annotation.Inject
import com.app.covi_di.annotation.Qualifier
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.data.mapper.toProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

// TODO: Step2 - CartProductDao를 참조하도록 변경
@Qualifier
class CartRepositoryImpl @Inject constructor(private val cartProduct: CartProductDao) :
    CartRepository {
    override suspend fun addCartProduct(product: Product) {
        cartProduct.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<Product> {
        return cartProduct.getAll().map { it.toProduct() }
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProduct.delete(id)
    }
}
