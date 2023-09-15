package woowacourse.shopping

import com.re4rk.arkdiAndroid.ArkApplication
import com.re4rk.arkdiAndroid.arkModules.arkModules
import woowacourse.shopping.di.ActivityModule
import woowacourse.shopping.di.ApplicationModule
import woowacourse.shopping.di.RetainedActivityModule
import woowacourse.shopping.di.ViewModelModule

class AppApplication : ArkApplication(
    arkModules = arkModules {
        applicationModule = { applicationContext -> ApplicationModule(applicationContext) }
        retainedActivityModule = { parent, context -> RetainedActivityModule(parent, context) }
        activityModule = { parent, context -> ActivityModule(parent, context) }
        viewModelModule = { parent -> ViewModelModule(parent) }
    },
)
