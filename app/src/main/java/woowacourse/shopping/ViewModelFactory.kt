package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

inline fun <reified VM : ViewModel> viewModelFactory(noinline scopeModule: () -> Any = { Any() }): ViewModelProvider.Factory =
    viewModelFactory {
        initializer<VM> {
            val smileDi = (this[APPLICATION_KEY] as DiApplication).smileDi
            val viewModelDi = smileDi.createChild(scopeModule(), ScopeContainer())
            viewModelDi.createDependency(VM::class)
        }
    }
