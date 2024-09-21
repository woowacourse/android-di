package woowacourse.shopping.di

import com.example.di.Inject
import com.example.di.InstanceProvideModule
import com.example.di.Provides
import woowacourse.shopping.data.local.CartProductDao
import woowacourse.shopping.data.local.ShoppingDatabase

@InstanceProvideModule
object DatabaseModule {
    @Provides
    fun provideShoppingDatabase(): ShoppingDatabase {
        return ShoppingDatabase.instanceOrException
    }

    @Provides
    fun provideCartProductDao(
        @Inject shoppingDatabase: ShoppingDatabase,
    ): CartProductDao = shoppingDatabase.cartProductDao()
}
