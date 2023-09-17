package woowacourse.shopping.data

import com.app.covi_di.annotation.Inject
import com.app.covi_di.annotation.Qualifier
import com.app.covi_di.annotation.QualifierType
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

// TODO: Step2 - CartProductDao를 참조하도록 변경
@Qualifier(type = QualifierType.DB)
class CartRepositoryImpl @Inject constructor(private val cartProduct: CartProductDao) :
    CartRepository {
    override suspend fun addCartProduct(product: Product) {
        cartProduct.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartProduct.getAll().map { it.toDomain() }
    }

    override suspend fun deleteCartProduct(id: Long) {
        cartProduct.delete(id)
    }
}
