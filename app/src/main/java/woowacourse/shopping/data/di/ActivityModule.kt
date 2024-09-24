package woowacourse.shopping.data.di

import android.content.Context
import com.woowacourse.di.DependencyContainer
import com.woowacourse.di.Module
import woowacourse.shopping.ui.cart.DateFormatter

class ActivityModule(private val context: Context) : Module {
    override fun install() {
        provideDateFormatter()
    }

    override fun clear() {
        DependencyContainer.removeInstance(DateFormatter::class)
    }

    private fun provideDateFormatter() {
        DependencyContainer.addInstance(
            classType = DateFormatter::class,
            instance = DateFormatter(context),
        )
    }
}
