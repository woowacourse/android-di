package woowacourse.shopping

import android.content.Context
import com.re4rk.arkdi.DiContainer
import com.re4rk.arkdiAndroid.DiGenerator
import woowacourse.shopping.di.DiActivityModule
import woowacourse.shopping.di.DiApplication
import woowacourse.shopping.di.DiApplicationModule
import woowacourse.shopping.di.DiRetainedActivityModule
import woowacourse.shopping.di.DiViewModelModule

class AppApplication : DiApplication() {
    override val diGenerator = object : DiGenerator {
        override fun createApplicationModule(applicationContext: Context): DiApplicationModule =
            DiApplicationModule(applicationContext)

        override fun createRetainedActivityModule(
            parentDiModule: DiContainer,
            context: Context,
        ): DiRetainedActivityModule = DiRetainedActivityModule(parentDiModule, context)

        override fun createActivityModule(
            parentDiModule: DiContainer,
            context: Context,
        ): DiActivityModule = DiActivityModule(parentDiModule, context)

        override fun createViewModelModule(parentDiModule: DiContainer): DiViewModelModule =
            DiViewModelModule(parentDiModule)
    }
}
