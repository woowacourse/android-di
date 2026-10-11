package woowacourse.shopping.ui.cart

import androidx.lifecycle.ViewModel
import woowacourse.shopping.di.DependencyContainer

class CartScreenScopeViewModel : ViewModel() {
    @field:DependencyContainer.Inject
    lateinit var dateFormatter: DateFormatter
}
