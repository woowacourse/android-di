package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.annotation.InMemoryCart
import woowacourse.shopping.data.annotation.RoomCart
import woowacourse.shopping.di.DIContainer

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DIContainer.initialize(this)
        DIContainer.bind(
            type = CartRepository::class,
            implementation = DefaultCartRepository::class,
            qualifier = RoomCart::class,
        )
        DIContainer.bind(
            type = CartRepository::class,
            implementation = InMemoryCartRepository::class,
            qualifier = InMemoryCart::class,
        )
    }
}
