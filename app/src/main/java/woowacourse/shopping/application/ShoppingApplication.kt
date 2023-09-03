package woowacourse.shopping.application

import android.app.Application
import woowacourse.shopping.di.container.SampleContainer
import woowacourse.shopping.di.container.ShoppingContainer

class ShoppingApplication : Application() {
    val appContainer: ShoppingContainer = SampleContainer()
}
