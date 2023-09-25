package woowacourse.shopping

import woowacourse.shopping.di.module.ActivityRetainedModule
import woowacourse.shopping.di.module.ApplicationModule
import woowacourse.shopping.di.module.ViewModelModule
import woowacourse.shopping.ui.DIApplication

class ShoppingApplication : DIApplication() {
    init {
        setModules(
            applicationModule = ApplicationModule::class,
            activityRetainedModule = ActivityRetainedModule::class,
            viewModelModule = ViewModelModule::class,
        )
    }
}
