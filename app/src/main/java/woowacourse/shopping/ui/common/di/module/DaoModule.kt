package woowacourse.shopping.ui.common.di.module

import android.content.Context
import androidx.room.Room
import com.woowacourse.bunadi.module.Module
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

class DaoModule(private val context: Context) : Module {
    fun provideCartProductDao(): CartProductDao = Room.databaseBuilder(
        context,
        ShoppingDatabase::class.java,
        ShoppingDatabase.DATABASE_NAME,
    ).build().cartProductDao()
}
