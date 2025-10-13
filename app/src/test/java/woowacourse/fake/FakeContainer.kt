package woowacourse.fake

import androidx.lifecycle.ViewModelProvider
import woowacourse.peto.di.DependencyContainer
import woowacourse.peto.di.ViewModelFactoryInjector
import woowacourse.shopping.Container

class FakeContainer : Container {
    private val databaseModule = FakeDatabaseModule()
    private val repositoryModule = FakeRepositoryModule(databaseModule.dao)

    override val dependencyContainer: DependencyContainer =
        DependencyContainer(
            listOf(
                databaseModule,
                repositoryModule,
            ),
        )

    override val viewModelFactory: ViewModelProvider.Factory =
        ViewModelFactoryInjector(dependencyContainer)
}
