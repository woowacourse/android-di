package woowacourse.shopping

import android.app.Application
import com.m6z1.moongdi.AutoDIViewModelFactory
import com.m6z1.moongdi.annotation.InMemory
import com.m6z1.moongdi.annotation.RoomDb
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
    @InMemory
    override val inMemoryCartRepository: CartRepository by lazy {
        InMemoryCartRepository()
    }

    @RoomDb
    override val roomCartRepository: CartRepository by lazy {
        DefaultCartRepository(ShoppingDatabase.getDatabase(this).cartProductDao())
    }

    @InMemory
    override val productRepository: ProductRepository by lazy {
        DefaultProductRepository()
    }

    val viewModelFactory = AutoDIViewModelFactory<AppDependencies>()
}
