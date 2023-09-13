package woowacourse.shopping.data

import woowacourse.shopping.annotation.InMemoryCartRepository
import woowacourse.shopping.annotation.InMemoryDataSource
import woowacourse.shopping.annotation.RoomDBCartRepository
import woowacourse.shopping.annotation.RoomDBDataSource
import woowacourse.shopping.application.ShoppingApplication
import woowacourse.shopping.data.datasource.CartDataSource
import woowacourse.shopping.data.datasource.DefaultCartDataSource
import woowacourse.shopping.data.datasource.InMemoryCartDataSource
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object Container {

    private val applicationContext = ShoppingApplication.getApplicationContext()

    fun getProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }

    @InMemoryCartRepository
    fun getInMemoryCartRepository(
        @InMemoryDataSource cartDataSource: CartDataSource,
    ): CartRepository {
        return DefaultCartRepository(cartDataSource)
    }

    @RoomDBCartRepository
    fun getRoomDBCartRepository(
        @RoomDBDataSource cartDataSource: CartDataSource,
    ): CartRepository {
        return DefaultCartRepository(cartDataSource)
    }

    @InMemoryDataSource
    fun getRoomDBCartDataSource(): CartDataSource {
        return InMemoryCartDataSource()
    }

    @RoomDBDataSource
    fun getInMemoryCartDataSource(
        cartProductDao: CartProductDao,
    ): CartDataSource {
        return DefaultCartDataSource(cartProductDao)
    }

    fun getCartDao(): CartProductDao {
        return ShoppingDatabase.getInstance(applicationContext).cartProductDao()
    }
}
