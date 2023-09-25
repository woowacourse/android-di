package woowacourse.shopping.data.di

import android.content.Context
import com.hyegyeong.di.DiModule
import com.hyegyeong.di.annotations.Inject
import com.hyegyeong.di.annotations.Singleton
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.RoomDBCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository

class AppModule(private val context: Context) : DiModule {
    fun provideInMemoryProductRepository(): ProductRepository =
        woowacourse.shopping.data.InMemoryProductRepository()
    fun provideContext(): Context = context

    fun provideCartProductDao(@Inject context: Context): CartProductDao =
        ShoppingDatabase.getDatabase(context).cartProductDao()

    @Singleton
    @InMemoryCartRepository
    fun provideInMemoryCartRepository(): CartRepository =
        woowacourse.shopping.data.InMemoryCartRepository()

    @Singleton
    @DatabaseCartRepository
    fun provideDataBaseCartRepository(@Inject dao: CartProductDao): CartRepository =
        RoomDBCartRepository(dao)
}
