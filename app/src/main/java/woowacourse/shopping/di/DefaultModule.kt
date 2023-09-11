package woowacourse.shopping.di

import android.app.Application
import androidx.room.Room
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DataBaseCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object DefaultModule : Module {
    @Singleton
    fun provideApplication(): Application {
        return ShoppingApplication.instance
    }

    @Singleton
    fun provideAppDatabase(): ShoppingDatabase {
        return Room.databaseBuilder(
            get<Application>().applicationContext,
            ShoppingDatabase::class.java, "cart_product"
        ).build()
    }

    @Singleton
    fun provideCartProductDao(): CartProductDao {
        return get<ShoppingDatabase>().cartProductDao()
    }

    @Singleton
    fun provideDispatcherIO(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Singleton
    fun provideSampleProducts(): List<Product> {
        return listOf(
            Product(
                name = "우테코 과자",
                price = 10_000,
                imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
            ),
            Product(
                name = "우테코 쥬스",
                price = 8_000,
                imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/52dca718-31c5-4f80-bafa-7e300d8c876a.jpg?h=700&w=700",
            ),
            Product(
                name = "우테코 아이스크림",
                price = 20_000,
                imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/e703c53e-5d01-4b20-bd33-85b5e778e73f.jpg?h=700&w=700",
            ),
        )
    }

    @Singleton
    fun provideProductRepository(): ProductRepository {
        return inject<DefaultProductRepository>()
    }

    @Qualifier("Database")
    fun provideDatabaseCartRepository(): CartRepository {
        return inject<DataBaseCartRepository>()
    }

    @Singleton
    @Qualifier("InMemory")
    fun provideInMemoryCartRepository(): CartRepository {
        return inject<InMemoryCartRepository>()
    }
}
