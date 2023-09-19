package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import com.created.customdi.annotation.ApplicationContext
import com.created.customdi.annotation.Singleton
import woowacourse.shopping.data.database.ShoppingDatabase

object DatabaseModule {

    @Singleton
    fun provideCartProductDao(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(
        context,
        ShoppingDatabase::class.java,
        "ShoppingDatabase",
    ).build().cartProductDao()
}
