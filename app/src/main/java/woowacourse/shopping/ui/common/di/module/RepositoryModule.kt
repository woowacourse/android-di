package woowacourse.shopping.ui.common.di.module

import android.content.Context
import androidx.room.Room
import com.buna.di.module.Module
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

class RepositoryModule(private val context: Context) : Module {
    fun provideCartProductDao(): CartProductDao = Room.databaseBuilder(
        context,
        ShoppingDatabase::class.java,
        ShoppingDatabase.DATABASE_NAME,
    ).build().cartProductDao()
}
