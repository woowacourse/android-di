package woowacourse.shopping.application

import woowacourse.shopping.di.application.DIApplication
import woowacourse.shopping.di.module.DefaultActivityModule
import woowacourse.shopping.di.module.DefaultApplicationModule
import woowacourse.shopping.di.module.DefaultViewModelModule

class ShoppingApplication : DIApplication(
    DefaultApplicationModule::class.java,
    DefaultActivityModule::class.java,
    DefaultViewModelModule::class.java,
)
