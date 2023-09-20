package woowacourse.shopping

import com.re4rk.arkdiAndroid.ArkApplication
import com.re4rk.arkdiAndroid.arkModules.arkModules
import woowacourse.shopping.di.ActivityModule
import woowacourse.shopping.di.ApplicationModule
import woowacourse.shopping.di.RetainedActivityModule
import woowacourse.shopping.di.ViewModelModule

class AppApplication : ArkApplication(
    arkModules = arkModules {
        applicationModule = ::ApplicationModule
        retainedActivityModule = ::RetainedActivityModule
        activityModule = ::ActivityModule
        viewModelModule = ::ViewModelModule
    },
)
