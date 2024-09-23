package woowacourse.shopping.di

import android.app.Application
import android.content.Context
import com.example.di.AppModule
import com.example.di.annotations.ActivityContext
import com.example.di.annotations.ActivityScope
import com.example.di.annotations.ApplicationScope
import com.example.di.annotations.ViewModelScope
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter

object DefaultAppModule : AppModule {
    @ApplicationScope
    fun provideApplication(): Application = ShoppingApplication.instance

    @ApplicationScope
    fun provideDatabase(application: Application): ShoppingDatabase = application.shoppingDatabase()

    private fun Application.shoppingDatabase(): ShoppingDatabase = ShoppingDatabase.getInstance(this)

    @ApplicationScope
    fun provideCartProductDao(database: ShoppingDatabase) = database.cartProductDao()

    @ActivityScope
    fun provideDateFormatter(
        @ActivityContext context: Context,
    ): DateFormatter = DateFormatter(context)

    @ViewModelScope
    fun provideProductRepository(): ProductRepository = DefaultProductRepository()

    @ApplicationScope
    @InMemoryRepository
    fun provideInMemoryCartRepository(): CartRepository = InMemoryCartRepository()

    @ApplicationScope
    @DatabaseRepository
    fun provideDatabaseCartRepository(cartProductDao: CartProductDao): CartRepository =
        DefaultCartRepository(cartProductDao = cartProductDao)
}
