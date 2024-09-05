package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.di.DIContainer

class ShoppingApplication : Application() {
    init {
        DIContainer.putInstance(DefaultProductRepository())
        DIContainer.putInstance(DefaultCartRepository())
    }
}
