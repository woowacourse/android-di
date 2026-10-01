package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.di.Injector
import woowacourse.di.get
import woowacourse.di.injector
import woowacourse.di.singleton
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase

fun shoppingInjector(context: Context): Injector =
    injector {
        singleton<ShoppingDatabase> {
            Room
                .databaseBuilder(
                    context.applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping.db",
                ).build()
        }
        singleton<CartProductDao> { get<ShoppingDatabase>().cartProductDao() }
        singleton<CartRepository>(RoomCart::class) { get<DefaultCartRepository>() }
        singleton<CartRepository>(InMemoryCart::class) { get<InMemoryCartRepository>() }
    }
