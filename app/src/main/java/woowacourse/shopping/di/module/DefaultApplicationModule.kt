package woowacourse.shopping.di.module

import android.content.Context
import androidx.room.Room
import com.example.di.module.ApplicationModule
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.dataSorce.DefaultLocalDataSource
import woowacourse.shopping.data.dataSorce.InMemoryLocalDataSource
import woowacourse.shopping.data.dataSorce.LocalDataSource
import woowacourse.shopping.di.annotation.InMemory
import woowacourse.shopping.di.annotation.InMemoryCartRepository
import woowacourse.shopping.di.annotation.RoomDb
import woowacourse.shopping.di.annotation.RoomDbCartRepository

class DefaultApplicationModule(applicationContext: Context) :
    ApplicationModule(applicationContext) {
    @InMemoryCartRepository
    fun getInMemoryCartRepository(@InMemory localDataSource: LocalDataSource): CartRepository {
        return DefaultCartRepository(localDataSource)
    }

    @RoomDbCartRepository
    fun getRoomCartRepository(@RoomDb localDataSource: LocalDataSource): CartRepository {
        return DefaultCartRepository(localDataSource)
    }

    @RoomDb
    fun getRoomDataSource(cartProductDao: CartProductDao): LocalDataSource {
        return DefaultLocalDataSource(cartProductDao)
    }

    @InMemory
    fun getInMemoryDataSource(): LocalDataSource {
        return InMemoryLocalDataSource()
    }

    fun getCartDao(): CartProductDao {
        return Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java,
            SHOPPING_DATABASE_NAME,
        ).build().cartProductDao()
    }

    companion object {
        private const val SHOPPING_DATABASE_NAME = "shopping_db_name"
    }
}
