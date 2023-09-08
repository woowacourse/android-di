package woowacourse.shopping

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.buna.di.module.Module
import com.buna.di.modules
import com.buna.di.types
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.ui.common.di.qualifier.DatabaseDao
import woowacourse.shopping.ui.common.di.qualifier.InMemoryDao

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        modules(
            DaoModule(this@ShoppingApplication),
        )

        types(
            ProductRepository::class to DefaultProductRepository::class,
            CartRepository::class to DefaultCartRepository::class,
        )
    }
}

class DaoModule(private val context: Context) : Module {
    @DatabaseDao
    fun provideDatabaseCartProductDao(): CartProductDao = Room.databaseBuilder(
        context,
        ShoppingDatabase::class.java,
        ShoppingDatabase.DATABASE_NAME,
    ).build().cartProductDao()

    @InMemoryDao
    fun provideInMemoryCartProductDao(): CartProductDao = Room.inMemoryDatabaseBuilder(
        context,
        ShoppingDatabase::class.java,
    ).build().cartProductDao()
}
