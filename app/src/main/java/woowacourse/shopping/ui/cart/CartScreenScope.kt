package woowacourse.shopping.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

internal fun interface CartScreenScopeFactory {
    fun factoryFor(scopeId: String): ViewModelProvider.Factory
}

internal class CartScreenScopeViewModel(
    val dateFormatter: DateFormatter,
    closeable: AutoCloseable,
) : ViewModel(closeable)
