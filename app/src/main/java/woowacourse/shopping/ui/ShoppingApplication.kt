package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.core.di.NewDependencyContainer.register
import woowacourse.shopping.data.CartProductDao

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        register(CartProductDao::class) { CartProductDao(this) }
    }
}
