package woowacourse.shopping.ui.common

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.shopping.di.DependencyInjector

class AppViewModelFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null,
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <VM : ViewModel> create(
        key: String,
        modelClass: Class<VM>,
        handle: SavedStateHandle,
    ): VM {
        val instance = DependencyInjector.injectConstructor(modelClass)
        return DependencyInjector.injectFields(instance)
    }
}
