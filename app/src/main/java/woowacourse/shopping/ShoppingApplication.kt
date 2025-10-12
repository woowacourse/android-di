package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import com.m6z1.moongdi.AutoDIViewModelFactory
import com.m6z1.moongdi.annotation.InMemory
import com.m6z1.moongdi.annotation.RoomDb
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.AppDependencies

class ShoppingApplication :
    Application(),
    AppDependencies {
    override val cartDao: CartProductDao by lazy {
        Room
            .databaseBuilder(
                applicationContext,
                ShoppingDatabase::class.java,
                "shopping.db",
            ).build()
            .cartProductDao()
    }

    @InMemory
    override val inMemoryCartRepository: CartRepository by lazy {
        InMemoryCartRepository()
    }

    @RoomDb
    override val roomCartRepository: CartRepository by lazy {
        DefaultCartRepository(cartDao)
    }

    @InMemory
    override val productRepository: ProductRepository by lazy {
        DefaultProductRepository()
    }

    val viewModelFactory = AutoDIViewModelFactory<AppDependencies>()
}
