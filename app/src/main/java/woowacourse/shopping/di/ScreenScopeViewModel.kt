package woowacourse.shopping.di

import androidx.lifecycle.ViewModel

class ScreenScopeViewModel : ViewModel() {
    val scope = AppDI.openScreenScope()

    init {
        addCloseable(scope)
    }
}
