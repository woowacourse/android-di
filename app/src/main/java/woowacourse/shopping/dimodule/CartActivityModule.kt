package woowacourse.shopping.dimodule

import android.content.Context
import com.bandal.fullmoon.FullMoonInject
import com.bandal.halfmoon.AndroidDependencyModule
import woowacourse.shopping.ui.cart.DateFormatter
import javax.inject.Singleton

class CartActivityModule : AndroidDependencyModule {

    override var context: Context? = null

    @Singleton
    fun createDateFormatter(
        @FullMoonInject
        context: Context,
    ) = DateFormatter(context)

    override fun getContext(): Context =
        context ?: throw NullPointerException("context가 초기화 되지 않았습니다.")
}
