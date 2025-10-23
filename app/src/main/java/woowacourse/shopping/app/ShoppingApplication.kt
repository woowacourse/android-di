package woowacourse.shopping.app

import android.app.Application
import android.content.Context
import woowacourse.shopping.app.ui.cart.DateFormatter
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.AppContainerImpl
import woowacourse.shopping.di.Container
import woowacourse.shopping.di.Database
import woowacourse.shopping.di.resolve
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.full.createInstance
import kotlin.reflect.typeOf

class ShoppingApplication : Application() {
    private val appContainer: AppContainerImpl by lazy {
        AppContainerImpl()
    }

    private val container: Container
        get() = appContainer

    override fun onCreate() {
        super.onCreate()

        configureContainer(appContainer, applicationContext)
    }

    private fun configureContainer(
        container: AppContainerImpl,
        context: Context,
    ) {
        container.register(
            type = typeOf<ShoppingDatabase>(),
            implementationClass = ShoppingDatabase::class,
        ) { _ ->
            ShoppingDatabase.getInstance(context)
        }

        container.register(
            type = typeOf<CartProductDao>(),
            implementationClass = CartProductDao::class,
        ) { di ->
            val database =
                di.resolve<ShoppingDatabase>()
                    ?: throw IllegalStateException("ShoppingDatabase가 등록되지 않았습니다")
            database.cartProductDao()
        }

        container.register(
            type = typeOf<ProductRepository>(),
            implementationClass = DefaultProductRepository::class,
            qualifier = Database::class.createInstance(),
        ) { _ ->
            DefaultProductRepository()
        }

        container.register(
            type = typeOf<DateFormatter>(),
            implementationClass = DateFormatter::class,
        ) { _ ->
            DateFormatter(context)
        }

        container.register(
            type = typeOf<CartRepository>(),
            implementationClass = DefaultCartRepository::class,
            qualifier = Database::class.createInstance(),
        ) { di ->
            val dao =
                di.resolve<CartProductDao>()
                    ?: throw IllegalStateException("CartProductDao가 등록되지 않았습니다")
            DefaultCartRepository(dao)
        }
    }

    companion object {
        fun getContainer(context: Context): Container = (context.applicationContext as ShoppingApplication).container
    }
}
