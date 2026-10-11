package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ScreenScope
import woowacourse.shopping.appScopeKey
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.ScopeKey
import kotlin.jvm.java
import kotlin.reflect.KClass

object ScreenEntryIdKey : CreationExtras.Key<String>
object DIContainerKey : CreationExtras.Key<DIContainer>

class ScreenScopeViewModel(
    entryId: String,
    private val diContainer: DIContainer,
) : ViewModel() {
    val scopeKey = ScopeKey(ScreenScope, entryId)

    init {
        diContainer.openScope(scopeKey, parentKey = appScopeKey)
        addCloseable { diContainer.closeScope(scopeKey) }
    }
}

object ScreenScopeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        require(modelClass == ScreenScopeViewModel::class.java)
        val entryId = requireNotNull(extras[ScreenEntryIdKey])
        val diContainer = requireNotNull(extras[DIContainerKey])
        return modelClass.cast(ScreenScopeViewModel(entryId, diContainer))
    }
}
