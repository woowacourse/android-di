package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DiContainer
import woowacourse.shopping.di.DiModule

class ShoppingApplication : Application() {
    val diContainer: DiContainer = DiContainer(DiModule)
}
