package woowacourse.shopping

import android.app.Application
import android.content.Context
import shopping.di.DIContainer
import shopping.di.QualifierType
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
            clazz = CartProductDao::class.java,
            instance = database.cartProductDao(),
            qualifier = QualifierType.ROOM_DB,
            scope = Scope.APP
        )
        DIContainer.register(
            clazz = CartProductDao::class.java,
            instance = InMemoryCartProductDao(),
            qualifier = QualifierType.IN_MEMORY,
            scope = Scope.APP
        )

        DIContainer.register(
            clazz = ProductRepository::class.java,
            instance = ProductRepository(),
            scope = Scope.VIEWMODEL
        )
        DIContainer.register(
            clazz = CartRepository::class.java,
            instance = CartRepository(database.cartProductDao()),
            scope = Scope.APP
        )
    }
}
