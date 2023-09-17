package woowacourse.shopping.data.di

import android.content.Context
import com.hyegyeong.di.DependencyContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.RoomDBCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository

class AppDependencyContainer(private val context: Context) : DependencyContainer {

    fun provideCartProductDao(): CartProductDao =
        ShoppingDatabase.getDatabase(context).cartProductDao()

    @InMemoryCartRepository
    fun provideInMemoryCartRepository(): CartRepository =
        woowacourse.shopping.data.InMemoryCartRepository()

    @DatabaseCartRepository
    fun provideDataBaseCartRepository(dao: CartProductDao): CartRepository =
        RoomDBCartRepository(dao)

    @InMemoryProductRepository
    fun provideInMemoryProductRepository(): ProductRepository =
        woowacourse.shopping.data.InMemoryProductRepository()
}