package woowacourse.shopping.data.di

import android.content.Context
import com.hyegyeong.di.DiModule
import com.hyegyeong.di.annotations.Singleton
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.RoomDBCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository

class AppDiModule(override var context: Context?) : DiModule {

    fun provideCartProductDao(): CartProductDao =
        ShoppingDatabase.getDatabase(context!!).cartProductDao()

    @Singleton
    @InMemoryCartRepository
    fun provideInMemoryCartRepository(): CartRepository =
        woowacourse.shopping.data.InMemoryCartRepository()

    @Singleton
    @DatabaseCartRepository
    fun provideDataBaseCartRepository(dao: CartProductDao): CartRepository =
        RoomDBCartRepository(dao)

    fun provideInMemoryProductRepository(): ProductRepository =
        woowacourse.shopping.data.InMemoryProductRepository()
}