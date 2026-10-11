package woowacourse.shopping.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ScreenScope
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.appScopeKey
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.DependencyKey
import woowacourse.shopping.di.ScopeKey
import kotlin.jvm.java

object ScreenEntryIdKey : CreationExtras.Key<String>

object DIContainerKey : CreationExtras.Key<DIContainer>

class CartScreenScopeViewModel(
    val dateFormatter: DateFormatter,
    closeScope: () -> Unit,
) : ViewModel() {
    init {
        addCloseable { closeScope() }
    }
}

object CartScreenScopeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        require(modelClass == CartScreenScopeViewModel::class.java)

        val entryId = requireNotNull(extras[ScreenEntryIdKey])
        val application =
            extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                as? ShoppingApplication
                ?: error("ShoppingApplication을 찾을 수 없습니다")
        val diContainer = application.diContainer
        val scopeKey = ScopeKey(ScreenScope, entryId)

        diContainer.openScope(scopeKey, parentKey = appScopeKey)
        try {
            val dateFormatter =
                diContainer.createInstance(
                    DependencyKey(DateFormatter::class),
                    scopeKey,
                )
            return modelClass.cast(
                CartScreenScopeViewModel(dateFormatter) {
                    diContainer.closeScope(scopeKey)
                },
            )
        } catch (error: Throwable) {
            diContainer.closeScope(scopeKey)
            throw error
        }
    }
}
