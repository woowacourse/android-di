package woowacourse.shopping

import android.content.Context
import woowacourse.shopping.core.Container
import woowacourse.shopping.core.DependencyModule
import woowacourse.shopping.data.db.DatabaseModule
import woowacourse.shopping.data.db.ShoppingDatabase
import woowacourse.shopping.data.repository.RepositoryModule
import woowacourse.shopping.di.DependencyInjector

class DependencyContainer(
    context: Context,
) : Container {
    private val shoppingDatabase: ShoppingDatabase = ShoppingDatabase.getInstance(context)
    private val databaseModule = DatabaseModule(shoppingDatabase)
    private val repositoryModule: DependencyModule = RepositoryModule(databaseModule)

    val dependencyInjector: DependencyInjector =
        DependencyInjector(
            listOf(
                databaseModule,
                repositoryModule,
            ),
        )
}
