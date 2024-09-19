package woowacourse.shopping

import android.app.Application
import android.content.Context
import shopping.di.DIContainer
import shopping.di.Scope
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DatabaseProvider
import woowacourse.shopping.data.InMemoryCartProductDao
import woowacourse.shopping.data.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupDI(this.applicationContext)
    }

    private fun setupDI(context: Context) {
        val database = DatabaseProvider.getDatabase(context)
        DIContainer.register(
            CartProductDao::class.java,
            database.cartProductDao(),
            "RoomDB",
            scope = Scope.APP
        )
        DIContainer.register(
            CartProductDao::class.java,
            InMemoryCartProductDao(),
            "InMemory",
            scope = Scope.APP
        )

        DIContainer.register(
            ProductRepository::class.java,
            ProductRepository(),
            scope = Scope.VIEWMODEL
        )
        DIContainer.register(
            CartRepository::class.java,
            CartRepository(database.cartProductDao()),
            scope = Scope.APP
        )
    }
}
