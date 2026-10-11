package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.di.DependencyScope
import woowacourse.di.KirbyDIContainer

internal class NavEntryScopeViewModel(
    val scope: DependencyScope,
) : ViewModel(scope) {
    companion object {
        fun factory(container: KirbyDIContainer): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    NavEntryScopeViewModel(container.createScope())
                }
            }
    }
}
