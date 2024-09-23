package woowacourse.shopping.di

import org.library.haeum.di.Module
import woowacourse.shopping.ui.cart.DateFormatter

class ActivityModule : Module() {
    fun provideDateFormatter(): DateFormatter = DateFormatter(context ?: throw IllegalArgumentException("Context가 null입니다."))
}
