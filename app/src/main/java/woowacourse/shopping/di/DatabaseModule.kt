package woowacourse.shopping.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

@SuppressLint("StaticFieldLeak")
object DatabaseModule {
    private val context: Context
        get() = ShoppingApplication.context

    private val database: ShoppingDatabase by lazy {
        Room
            .databaseBuilder(
                context,
                ShoppingDatabase::class.java,
                "shopping.db",
            ).build()
    }

    val cartDao: CartProductDao by lazy { database.cartProductDao() }
}
