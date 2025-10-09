package woowacourse.shopping

import woowacourse.shopping.core.Container
import woowacourse.shopping.core.DependencyModule
import woowacourse.shopping.data.repository.RepositoryModule
import woowacourse.shopping.di.DependencyInjector

class DependencyContainer : Container {
    val repositoryModule: DependencyModule = RepositoryModule()

    val dependencyInjector: DependencyInjector =
        DependencyInjector(
            listOf(repositoryModule),
        )
}
