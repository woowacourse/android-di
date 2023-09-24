package woowacourse.shopping.di.module

import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ApplicationModule
import woowacourse.shopping.ui.cart.DateFormatter

class DefaultActivityRetainedModule(applicationModule: ApplicationModule) :
    ActivityRetainedModule(applicationModule) {
    fun getDateFormatter(): DateFormatter {
        return DateFormatter(applicationContext)
    }
}
