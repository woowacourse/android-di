package woowacourse.shopping.data

import android.content.Context
import com.bignerdranch.android.koala.Container
import woowacourse.shopping.annotation.DataBaseCartRepository
import woowacourse.shopping.annotation.DataBaseDataSource
import woowacourse.shopping.annotation.InMemoryCartRepository
import woowacourse.shopping.annotation.InMemoryDataSource
import woowacourse.shopping.data.datasource.CartDataSource
import woowacourse.shopping.data.datasource.DefaultCartDataSource
import woowacourse.shopping.data.datasource.InMemoryCartDataSource
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingContainer(private val context: Context) : Container {

    fun getProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }

    @InMemoryCartRepository
    fun getInMemoryCartRepository(
        @InMemoryDataSource cartDataSource: CartDataSource,
    ): CartRepository {
        return DefaultCartRepository(cartDataSource)
    }

    @DataBaseCartRepository
    fun getRoomDBCartRepository(
        @DataBaseDataSource cartDataSource: CartDataSource,
    ): CartRepository {
        return DefaultCartRepository(cartDataSource)
    }

    @InMemoryDataSource
    fun getInMemoryCartDataSource(): CartDataSource {
        return InMemoryCartDataSource()
    }

    @DataBaseDataSource
    fun getRoomDBCartDataSource(
        cartProductDao: CartProductDao,
    ): CartDataSource {
        return DefaultCartDataSource(cartProductDao)
    }

    fun getCartDao(): CartProductDao {
        return ShoppingDatabase.getInstance(context).cartProductDao()
    }
}
