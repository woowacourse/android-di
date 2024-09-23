package woowacourse.shopping.di

import android.content.Context
import com.kmlibs.supplin.annotations.ApplicationContext
import com.kmlibs.supplin.annotations.Concrete
import com.kmlibs.supplin.annotations.Module
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DBCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase

@Module
object CartModule {
    @Concrete
    fun provideShoppingDatabase(
        @ApplicationContext applicationContext: Context,
    ): ShoppingDatabase = ShoppingDatabase.getInstance(applicationContext)

    @Concrete
    @InMemoryRepository
    fun provideInMemoryCartRepository(): CartRepository = InMemoryCartRepository()

    @Concrete
    @DatabaseRepository
    fun provideCartRepository(database: ShoppingDatabase): CartRepository = DBCartRepository(database)
}
