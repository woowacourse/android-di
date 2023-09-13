package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Module
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

@Module
object AppModule {

    @Component
    fun getShoppingDatabase(context: Context): ShoppingDatabase = Room.databaseBuilder(
        context.applicationContext,
        ShoppingDatabase::class.java,
        "shopping_db.db",
    ).build()

    @Component
    fun getCartProductDao(shoppingDatabase: ShoppingDatabase): CartProductDao =
        shoppingDatabase.cartProductDao()
}
