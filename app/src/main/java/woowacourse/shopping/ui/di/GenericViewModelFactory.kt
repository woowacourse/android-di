package woowacourse.shopping.ui.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class GenericViewModelFactory<T : ViewModel>(
    private val viewModelClass: KClass<out T>,
    private val creator: (KClass<out T>) -> T,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        @Suppress("UNCHECKED_CAST")
        return creator(viewModelClass) as T
    }
}