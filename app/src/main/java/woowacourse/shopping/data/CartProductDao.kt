package woowacourse.shopping.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CartProductDao {
    @Query("SELECT * FROM cart_products")
    suspend fun getAll(): List<CartProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartProduct: CartProductEntity)

    @Query("DELETE FROM cart_products WHERE id = :id")
    suspend fun delete(id: Long)
}
