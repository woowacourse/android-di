package woowacourse.shopping

import com.re4rk.arkdiAndroid.ArkApplication
import com.re4rk.arkdiAndroid.arkGenerator.arkGenerator
import woowacourse.shopping.di.ActivityModule
import woowacourse.shopping.di.ApplicationModule
import woowacourse.shopping.di.RetainedActivityModule
import woowacourse.shopping.di.ViewModelModule

class AppApplication : ArkApplication() {
    override val arkGenerator = arkGenerator {
        applicationModule = { applicationContext -> ApplicationModule(applicationContext) }
        retainedActivityModule = { parent, context -> RetainedActivityModule(parent, context) }
        activityModule = { parent, context -> ActivityModule(parent, context) }
        viewModelModule = { parent -> ViewModelModule(parent) }
    }
}
