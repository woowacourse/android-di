package woowacourse.shopping

import woowacourse.shopping.core.Container
import woowacourse.shopping.data.repository.RepositoryModule
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.domain.DependencyModule

class DependencyContainer : Container {
    val repositoryModule: DependencyModule = RepositoryModule()

    val dependencyInjector: DependencyInjector =
        DependencyInjector(
            listOf(repositoryModule),
        )
}
