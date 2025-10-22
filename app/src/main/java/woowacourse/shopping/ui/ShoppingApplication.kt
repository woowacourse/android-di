package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.android.di.AndroidContainer
import woowacourse.shopping.android.di.Scope
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.PersistentCartRepository
import woowacourse.shopping.model.CartRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidContainer.register(CartProductDao::class, Scope.ApplicationScope) {
            CartProductDao(this)
        }

        AndroidContainer.register(
            CartRepository::class,
            Scope.ApplicationScope,
            PersistentCartRepository.QUALIFIER,
        ) {
            PersistentCartRepository(
                AndroidContainer.instance(
                    CartProductDao::class,
                    Scope.ApplicationScope,
                ),
            )
        }
    }
}
