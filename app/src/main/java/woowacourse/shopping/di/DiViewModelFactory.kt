package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harodi.DiManager
import com.harodi.ScopeKey

class DiViewModelFactory(
    private val diManager: DiManager,
    private val parentKey: ScopeKey,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelScopeKey =
            ScopeKey(
                parentKey = parentKey,
                scopeKind = Scope.VIEW_MODEL,
            )
        val viewModel = diManager.resolve(modelClass, viewModelScopeKey)

        viewModel.addCloseable {
            diManager.removeScope(viewModelScopeKey)
        }

        return viewModel
    }
}
