package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.AppContainer
import woowacourse.shopping.annotation.InMemory
import woowacourse.shopping.annotation.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

object DefaultAppContainer : AppContainer() {
    fun init(context: Context) {
        bind(ShoppingDatabase::class, ShoppingDatabase.getInstance(context.applicationContext))
        bind(CartProductDao::class, getInstance(ShoppingDatabase::class).cartProductDao())
        bind(ProductRepository::class, getInstance(ProductRepositoryImpl::class))
        bind(CartRepository::class, getInstance(DefaultCartRepository::class), Room::class)
        bind(CartRepository::class, getInstance(InMemoryCartRepository::class), InMemory::class)
    }
}
