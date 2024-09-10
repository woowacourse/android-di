package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.repository.CartRepositoryImpl
import woowacourse.shopping.data.repository.ProductRepositoryImpl
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.local.dao.CartProductDao
import woowacourse.shopping.local.db.ShoppingDatabase

class DIModule(private val context: Context) {
    fun provideCartProductDao(): CartProductDao {
        return ShoppingDatabase.getInstance(context).cartProductDao()
    }

    fun provideProductRepository(): ProductRepository {
        return ProductRepositoryImpl()
    }

    fun provideCartRepository(cartProductDao: CartProductDao): CartRepository {
        return CartRepositoryImpl(cartProductDao)
    }
}
