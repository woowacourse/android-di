package woowacourse.shopping.ui.base

import androidx.lifecycle.ViewModel
import shopping.di.DIContainer
import shopping.di.ScopeOwner

abstract class BaseViewModel : ViewModel(), ScopeOwner {

    init {
        DIContainer.createViewModelScope(this)
    }

    public override fun onCleared() {
        super.onCleared()
        DIContainer.clearViewModelScope(this)
    }
}
