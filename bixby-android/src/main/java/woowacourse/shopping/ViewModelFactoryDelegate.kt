package woowacourse.shopping

import androidx.lifecycle.ViewModelProvider

interface ViewModelFactoryDelegate {
    val Factory: ViewModelProvider.Factory
}
