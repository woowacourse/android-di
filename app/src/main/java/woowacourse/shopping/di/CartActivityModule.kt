package woowacourse.shopping.di

import android.content.Context
import woowacourse.di.module.AndroidModule
import woowacourse.shopping.ui.cart.DateFormatter

class CartActivityModule : AndroidModule {

    override lateinit var context: Context

    override fun setModuleContext(context: Context) {
        this.context = context
    }

    fun provideDateFormatter(): DateFormatter = DateFormatter(context)
}
