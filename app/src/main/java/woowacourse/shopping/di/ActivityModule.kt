package woowacourse.shopping.di

import android.content.Context
import woowacourse.di.Module
import woowacourse.shopping.ui.cart.DateFormatter

class ActivityModule : Module {

    override lateinit var context: Context

    override fun setModuleContext(context: Context) {
        this.context = context
    }

    fun provideDateFormatter(): DateFormatter = DateFormatter(context)
}
