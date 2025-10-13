package woowacourse.shopping

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import woowacourse.peto.di.DependencyContainer
import woowacourse.peto.di.DependencyModule
import woowacourse.peto.di.ViewModelFactoryInjector
import woowacourse.shopping.data.db.DatabaseModule
import woowacourse.shopping.data.db.ShoppingDatabase
import woowacourse.shopping.data.repository.RepositoryModule

class AppContainer(
    context: Context,
) : Container {
    private val shoppingDatabase: ShoppingDatabase = ShoppingDatabase.getInstance(context)
    private val databaseModule = DatabaseModule(shoppingDatabase)
    private val repositoryModule: DependencyModule = RepositoryModule(databaseModule)

    override val dependencyContainer: DependencyContainer =
        DependencyContainer(
            listOf(
                databaseModule,
                repositoryModule,
            ),
        )
    override val viewModelFactory: ViewModelProvider.Factory = ViewModelFactoryInjector(dependencyContainer)
}
