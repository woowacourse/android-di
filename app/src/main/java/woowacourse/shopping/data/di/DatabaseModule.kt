package woowacourse.shopping.data.di

import android.content.Context
import androidx.room.Room
import com.woowacourse.di.ApplicationContext
import com.woowacourse.di.Module
import woowacourse.shopping.data.ShoppingDatabase

@Module
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
