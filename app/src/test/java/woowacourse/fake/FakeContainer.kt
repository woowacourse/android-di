package woowacourse.fake

import androidx.lifecycle.ViewModelProvider
import woowacourse.peto.di.DependencyContainer
import woowacourse.peto.di.ViewModelFactoryInjector
import woowacourse.shopping.Container

class FakeContainer : Container {
    override val dependencyContainer: DependencyContainer =
        DependencyContainer(
            emptyList(),
        )
    override val viewModelFactory: ViewModelProvider.Factory =
        ViewModelFactoryInjector(dependencyContainer)
}
