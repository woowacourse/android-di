package woowacourse.shopping.ui.di

import android.content.Context
import androidx.room.Room
import io.hyemdooly.di.Module
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

class DaoModule(private val context: Context) : Module {
    fun provideCartProductDao(): CartProductDao = Room.databaseBuilder(
        context,
        ShoppingDatabase::class.java,
        ShoppingDatabase.name,
    ).build().cartProductDao()
}
