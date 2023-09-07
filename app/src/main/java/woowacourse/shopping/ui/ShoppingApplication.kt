package woowacourse.shopping.ui

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.Container

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        inject()
    }

    private fun inject() {
        val db = Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java,
            ShoppingDatabase.name,
        ).build()

        Container.addInstance(CartRepository::class, DefaultCartRepository(db.cartProductDao()))
        Container.addInstance(ProductRepository::class, DefaultProductRepository())
    }
}
