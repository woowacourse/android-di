package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import com.example.di.annotation.Inject
import com.example.di.annotation.Module
import com.example.di.annotation.Provides
import com.example.di.annotation.Singleton
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideShoppingDatabase(
        @Inject context: Context,
    ): ShoppingDatabase =
        Room
            .databaseBuilder(
                context.applicationContext,
                ShoppingDatabase::class.java,
                "shopping.db",
            ).build()

    @Provides
    @Singleton
    fun provideCartProductDao(
        @Inject db: ShoppingDatabase,
    ): CartProductDao = db.cartProductDao()
}
