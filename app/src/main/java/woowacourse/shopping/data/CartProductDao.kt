package woowacourse.shopping.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sh1mj1.Inject
import com.example.sh1mj1.Qualifier

@Inject
@Qualifier("RoomDao")
@Dao
interface CartProductDao : CartDao {
    @Query("SELECT * FROM cart_products")
    override suspend fun getAll(): List<CartProductEntity>

    @Insert
    override suspend fun insert(cartProduct: CartProductEntity)

    @Query("DELETE FROM cart_products WHERE id = :id")
    override suspend fun delete(id: Long)

    companion object {
        fun instance(context: Context): CartProductDao = ShoppingDatabase.instance(context).cartProductDao()
    }
}

interface CartDao {
    suspend fun getAll(): List<CartProductEntity>

    suspend fun insert(cartProduct: CartProductEntity)

    suspend fun delete(id: Long)
}
