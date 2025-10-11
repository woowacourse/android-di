package woowacourse.shopping.ui.util

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.di.AppContainer

class DIViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T =
        AppContainer.resolve(
            clazz = modelClass.kotlin,
            overrides = mapOf(SavedStateHandle::class to extras.createSavedStateHandle()),
        )
}
