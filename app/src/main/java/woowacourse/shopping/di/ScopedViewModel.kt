package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import woowacourse.di.DependencyContainer

abstract class ScopedViewModel: ViewModel() {
    private var scopeId: Long? = null

    internal fun attachScope(scopeId: Long) {
        this.scopeId = scopeId
    }

    override fun onCleared() {
        scopeId?.let { DependencyContainer.closeScope(it) }
        super.onCleared()
    }
}