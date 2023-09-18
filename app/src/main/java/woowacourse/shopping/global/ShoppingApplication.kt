package woowacourse.shopping.global

import com.example.di.application.DiApplication
import woowacourse.shopping.di.module.DefaultActivityModule
import woowacourse.shopping.di.module.DefaultActivityRetainedModule
import woowacourse.shopping.di.module.DefaultApplicationModule
import woowacourse.shopping.di.module.DefaultViewModelModule

class ShoppingApplication : DiApplication(
    DefaultApplicationModule::class.java,
    DefaultActivityRetainedModule::class.java,
    DefaultViewModelModule::class.java,
    DefaultActivityModule::class.java,
)
