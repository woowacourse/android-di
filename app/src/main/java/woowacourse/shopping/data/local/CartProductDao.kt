package woowacourse.shopping.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartProductDao {

    @Query("SELECT * FROM cart_products")
    suspend fun getAll(): Flow<List<CartProductEntity>>

    @Insert
    suspend fun insert(cartProduct: CartProductEntity)

    @Query("DELETE FROM cart_products WHERE id = :id")
    suspend fun delete(id: Long)
}
