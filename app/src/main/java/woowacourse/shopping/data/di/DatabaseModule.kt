package woowacourse.shopping.data.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.shoppingapp.di.ApplicationContext

class DatabaseModule {
    fun provideShoppingDatabase(
        @ApplicationContext context: Context,
    ): ShoppingDatabase =
        Room.databaseBuilder(
            context,
            ShoppingDatabase::class.java,
            DATABASE_NAME,
        ).fallbackToDestructiveMigration()
            .build()

    companion object {
        const val DATABASE_NAME = "ShoppingDatabase"
    }
}
