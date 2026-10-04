package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import woowacourse.di.DependencyScope

abstract class ScopedViewModel : ViewModel() {
    internal var dependencyScope: DependencyScope? = null

    override fun onCleared() {
        dependencyScope?.close()
        dependencyScope = null
        super.onCleared()
    }
}
