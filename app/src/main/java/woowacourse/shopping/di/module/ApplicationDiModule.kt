package woowacourse.shopping.di.module

import android.content.Context
import com.bignerdranch.android.koala.DiModule
import com.bignerdranch.android.koala.KoalaSingleton
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.datasource.CartDataSource
import woowacourse.shopping.data.datasource.DefaultCartDataSource
import woowacourse.shopping.data.datasource.InMemoryCartDataSource
import woowacourse.shopping.di.annotation.DataBaseCartRepository
import woowacourse.shopping.di.annotation.DataBaseDataSource
import woowacourse.shopping.di.annotation.InMemoryCartRepository
import woowacourse.shopping.di.annotation.InMemoryDataSource
import woowacourse.shopping.repository.CartRepository

class ApplicationDiModule : DiModule {

    override var context: Context? = null

    @KoalaSingleton
    @InMemoryCartRepository
    fun getInMemoryCartRepository(
        @InMemoryDataSource cartDataSource: CartDataSource,
    ): CartRepository {
        return DefaultCartRepository(cartDataSource)
    }

    @KoalaSingleton
    @DataBaseCartRepository
    fun getRoomDBCartRepository(
        @DataBaseDataSource cartDataSource: CartDataSource,
    ): CartRepository {
        return DefaultCartRepository(cartDataSource)
    }

    @KoalaSingleton
    @InMemoryDataSource
    fun getInMemoryCartDataSource(): CartDataSource {
        return InMemoryCartDataSource()
    }

    @KoalaSingleton
    @DataBaseDataSource
    fun getRoomDBCartDataSource(
        cartProductDao: CartProductDao,
    ): CartDataSource {
        return DefaultCartDataSource(cartProductDao)
    }

    @KoalaSingleton
    fun getCartDao(): CartProductDao {
        return context?.let {
            ShoppingDatabase.getInstance(it).cartProductDao()
        } ?: throw NullPointerException("context가 초기화되지 않았습니다")
    }
}
