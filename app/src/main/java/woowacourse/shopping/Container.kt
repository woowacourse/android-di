package woowacourse.shopping

import androidx.lifecycle.ViewModelProvider
import woowacourse.peto.di.DependencyContainer

interface Container {
    val dependencyContainer: DependencyContainer
    val viewModelFactory: ViewModelProvider.Factory
}
