package woowacourse.shopping

import woowacourse.shopping.di.AppModule
import woowacourse.shopping.di.CartActivityModule
import woowacourse.shopping.hasydi.DiApplication
import woowacourse.shopping.ui.cart.CartActivity

class ShoppingApplication : DiApplication(
    appModule = AppModule(),
    activityRetainedModule = listOf(CartActivity::class to CartActivityModule()),
)
