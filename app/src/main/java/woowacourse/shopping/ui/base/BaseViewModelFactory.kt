package woowacourse.shopping.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// TODO: 사용하고 있지 않음. 삭제해야 함.
class BaseViewModelFactory<VM : ViewModel>(
    private val creator: () -> VM,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = creator() as T
}
