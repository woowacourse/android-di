package woowacourse.shopping.application

import woowacourse.shopping.di.DIApplication
import woowacourse.shopping.di.DefaultActivityModule
import woowacourse.shopping.di.DefaultApplicationModule
import woowacourse.shopping.di.DefaultViewModelModule

class ShoppingApplication : DIApplication(
    DefaultApplicationModule::class.java,
    DefaultActivityModule::class.java,
    DefaultViewModelModule::class.java,
)
