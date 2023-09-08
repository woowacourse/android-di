package woowacourse.shopping

import android.app.Application
import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.di.injector.modules
import woowacourse.shopping.di.injector.type
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.ui.common.di.qualifier.DatabaseDao
import woowacourse.shopping.ui.common.di.qualifier.InMemoryDao

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 2. 어플리케이션에서 프로바이더 모듈을 맵에 등록
        modules(
            DaoModule(this@ShoppingApplication),
        )
        type(
            ProductRepository::class to DefaultProductRepository::class,
            CartRepository::class to DefaultCartRepository::class,
        )
    }
}

interface Module

// 1. 프로바이더 함수에 퀄리파리어 어노테이션 붙이기
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
