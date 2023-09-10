package woowacourse.shopping.ui.util

import androidx.lifecycle.ViewModelProvider

interface HasViewModelFactory {
    var viewModelFactory: ViewModelProvider.Factory
}
