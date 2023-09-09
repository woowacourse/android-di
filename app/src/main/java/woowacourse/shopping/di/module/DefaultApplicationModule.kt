package woowacourse.shopping.di.module

import android.content.Context
import androidx.room.Room
import com.example.di.module.ApplicationModule
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.MemoryCartRepository
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
        return getOrCreateInstance {
            MemoryCartRepository(localDataSource)
        }
    }

    @RoomDbCartRepository
    fun getRoomCartRepository(@RoomDb localDataSource: LocalDataSource): CartRepository {
        return getOrCreateInstance {
            DefaultCartRepository(localDataSource)
        }
    }

    @RoomDb
    fun getRoomDataSource(cartProductDao: CartProductDao): LocalDataSource {
        return getOrCreateInstance { DefaultLocalDataSource(cartProductDao) }
    }

    @InMemory
    fun getInMemoryDataSource(): LocalDataSource {
        return getOrCreateInstance { InMemoryLocalDataSource() }
    }

    fun getCartDao(): CartProductDao {
        return getOrCreateInstance {
            val applicationContext =
                context ?: throw IllegalStateException("애플리케이션이 아직 초기화되지 않았습니다")
            Room.databaseBuilder(
                applicationContext,
                ShoppingDatabase::class.java,
                SHOPPING_DATABASE_NAME,
            ).build().cartProductDao()
        }
    }

    companion object {
        private const val SHOPPING_DATABASE_NAME = "shopping_db_name"
    }
}
