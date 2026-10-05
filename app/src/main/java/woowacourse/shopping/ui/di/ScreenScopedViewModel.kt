package woowacourse.shopping.ui.di

import androidx.lifecycle.ViewModel
import io.github.firstwoosun.di.DependencyScope

class ScreenScopedViewModel(
    val scope: DependencyScope,
): ViewModel() {
    init {
        addCloseable(scope)
    }
}