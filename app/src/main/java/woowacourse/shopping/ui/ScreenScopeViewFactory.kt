package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ScreenScope
import woowacourse.shopping.appScopeKey
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.ScopeKey
import kotlin.jvm.java

object ScreenEntryIdKey : CreationExtras.Key<String>

class ScreenScopeViewModel(
    entryId: String,
) : ViewModel() {
    val scopeKey = ScopeKey(ScreenScope, entryId)

    init {
        DIContainer.openScope(scopeKey, parentKey = appScopeKey)
        addCloseable { DIContainer.closeScope(scopeKey) }
    }
}

object ScreenScopeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        require(modelClass == ScreenScopeViewModel::class.java)
        val entryId = requireNotNull(extras[ScreenEntryIdKey])
        return modelClass.cast(ScreenScopeViewModel(entryId))
    }
}
