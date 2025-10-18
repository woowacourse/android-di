package woowacourse.shopping.data

import android.util.Log
import com.example.di.Remote
import com.example.di.Singleton
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.model.Product

@Remote
@Singleton
class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {

    init {
        Log.d("TAG", "DI 생명주기: DefaultCartRepository 생성")
    }

    override suspend fun addCartProduct(product: Product) {
        dao.insert(product.toEntity())
    }

    override suspend fun getAllCartProducts(): List<CartProductEntity> {
        return dao.getAll()
    }

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}
