package woowacourse.shopping.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CommonViewModelFactory(
    private val viewModelClass: Class<*>,
    private val provider: () -> ViewModel,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(viewModelClass)) {
            return provider() as T
        }
        throw IllegalArgumentException("ViewModel 타입이 맞지 않습니다.")
    }
}
