package woowacouse.shopping.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val diFactory: DiFactory,
    private val handle: SavedStateHandle,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel: T =
            diFactory.create(
                modelClass.simpleName ?: "",
                modelClass.kotlin,
                handle = handle,
            )
        return viewModel
    }
}
