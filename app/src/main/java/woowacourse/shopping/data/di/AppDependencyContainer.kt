package woowacourse.shopping.data.di

import android.content.Context
import com.hyegyeong.di.DependencyContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.RoomDBCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository

class AppDependencyContainer(private val context: Context) : DependencyContainer {
    private val inMemoryCartRepository =
        woowacourse.shopping.data.InMemoryCartRepository()

    fun provideCartProductDao(): CartProductDao =
        ShoppingDatabase.getDatabase(context).cartProductDao()

    @InMemoryCartRepository
    fun provideInMemoryCartRepository(): CartRepository = inMemoryCartRepository

    @DatabaseCartRepository
    fun provideDataBaseCartRepository(dao: CartProductDao): CartRepository =
        RoomDBCartRepository(dao)

    fun provideInMemoryProductRepository(): ProductRepository =
        woowacourse.shopping.data.InMemoryProductRepository()
}