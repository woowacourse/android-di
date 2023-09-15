package woowacourse.shopping

import android.content.Context
import com.re4rk.arkdi.ArkContainer
import com.re4rk.arkdiAndroid.ArkApplication
import com.re4rk.arkdiAndroid.ArkGenerator
import woowacourse.shopping.di.ActivityModule
import woowacourse.shopping.di.ApplicationModule
import woowacourse.shopping.di.RetainedActivityModule
import woowacourse.shopping.di.ViewModelModule

class AppApplication : ArkApplication() {
    override val arkGenerator = object : ArkGenerator {
        override fun createApplicationModule(applicationContext: Context): ApplicationModule =
            ApplicationModule(applicationContext)

        override fun createRetainedActivityModule(
            parentDiModule: ArkContainer,
            context: Context,
        ): RetainedActivityModule = RetainedActivityModule(parentDiModule, context)

        override fun createActivityModule(
            parentDiModule: ArkContainer,
            context: Context,
        ): ActivityModule = ActivityModule(parentDiModule, context)

        override fun createViewModelModule(parentDiModule: ArkContainer): ViewModelModule =
            ViewModelModule(parentDiModule)
    }
}
