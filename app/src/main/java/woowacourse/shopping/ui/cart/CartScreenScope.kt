package woowacourse.shopping.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.cksckckcks.di.ScopedContainer
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.di.ShoppingScopes
import kotlin.reflect.KClass

class CartScreenScopeViewModel(
    val dateFormatter: DateFormatter,
    scope: ScopedContainer,
) : ViewModel() {
    init {
        addCloseable(scope)
    }
}

class CartScreenScopeFactory(
    private val scopeId: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T {
        require(modelClass == CartScreenScopeViewModel::class)
        val application = extras[APPLICATION_KEY] as ShoppingApplication
        val scope = application.container.diContainer.openScope(ShoppingScopes.screen, scopeId)

        return try {
            val formatter = scope.getInstance(DateFormatter::class) as DateFormatter
            modelClass.java.cast(CartScreenScopeViewModel(formatter, scope))
        } catch (error: Throwable) {
            scope.close()
            throw error
        }
    }
}
