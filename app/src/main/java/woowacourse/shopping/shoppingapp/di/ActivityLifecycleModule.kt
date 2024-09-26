package woowacourse.shopping.shoppingapp.di

import android.content.Context
import com.woowacourse.di.DiContainer
import com.woowacourse.di.DiModule
import woowacourse.shopping.ui.cart.DateFormatter

class ActivityLifecycleModule(private val context: Context) : DiModule {
    fun bindDateFormatter(diContainer: DiContainer) {
        diContainer.bind(
            DateFormatter::class,
            DateFormatter(context),
        )
    }
}
