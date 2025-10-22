package woowacourse.shopping.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room

@Dao
interface CartProductDao {
    @Query("SELECT * FROM cart_products")
    suspend fun getAll(): List<CartProductEntity>

    @Insert
    suspend fun insert(cartProduct: CartProductEntity)

    @Query("DELETE FROM cart_products WHERE id = :id")
    suspend fun delete(id: Long)
}

fun CartProductDao(applicationContext: Context): CartProductDao {
    val database: ShoppingDatabase =
        Room
            .databaseBuilder(
                applicationContext,
                ShoppingDatabase::class.java,
                "cartProduct",
            ).build()

    return database.cartProductDao()
}
