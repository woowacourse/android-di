package woowacourse.shopping.data.dataSorce

import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

interface LocalDataSource {

    suspend fun getAll(): List<CartProduct>

    suspend fun insert(product: Product)

    suspend fun delete(id: Long)
}
