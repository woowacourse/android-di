package woowacourse.shopping.di

import android.content.Context
import com.kmlibs.supplin.annotations.ApplicationContext
import com.kmlibs.supplin.annotations.Concrete
import com.kmlibs.supplin.annotations.Module
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DBCartRepository
import woowacourse.shopping.data.ShoppingDatabase

@Module
object DatabaseModule {
    @Concrete
    fun provideShoppingDatabase(
        @ApplicationContext applicationContext: Context,
    ): ShoppingDatabase = ShoppingDatabase.getInstance(applicationContext)

    @Concrete
    @DatabaseRepository
    fun provideCartRepository(shoppingDatabase: ShoppingDatabase): CartRepository = DBCartRepository(shoppingDatabase.cartProductDao())
}
