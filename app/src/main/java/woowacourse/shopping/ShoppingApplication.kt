package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.ViewModelFactory

class ShoppingApplication : Application() {
    val viewModelFactory: ViewModelFactory by lazy {
        ViewModelFactory(AppContainer)
    }
}
