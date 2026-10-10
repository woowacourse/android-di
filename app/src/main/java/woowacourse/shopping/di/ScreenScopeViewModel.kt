package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.di.DIResolver
import woowacourse.di.ScopedContainer

internal class ScreenScopeViewModel(
    val resolver: ScopedContainer,
) : ViewModel(resolver)

internal class ScreenScopeViewModelFactory(
    private val parentResolver: DIResolver,
    private val screenId: String,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == ScreenScopeViewModel::class.java) {
            "지원하지 않는 ViewModel입니다: ${modelClass.name}"
        }
        val screenScope = parentResolver.openScope(ScreenScope, screenId)
        return ScreenScopeViewModel(screenScope) as T
    }
}
