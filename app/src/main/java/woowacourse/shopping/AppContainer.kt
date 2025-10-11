package woowacourse.shopping

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.db.DatabaseModule
import woowacourse.shopping.data.db.ShoppingDatabase
import woowacourse.shopping.data.repository.RepositoryModule
import woowacourse.shopping.di.DependencyContainer
import woowacourse.shopping.di.DependencyModule
import woowacourse.shopping.di.ViewModelFactoryInjector

class AppContainer(
    context: Context,
) {
    private val shoppingDatabase: ShoppingDatabase = ShoppingDatabase.getInstance(context)
    private val databaseModule = DatabaseModule(shoppingDatabase)
    private val repositoryModule: DependencyModule = RepositoryModule(databaseModule)

    val dependencyContainer: DependencyContainer =
        DependencyContainer(
            listOf(
                databaseModule,
                repositoryModule,
            ),
        )
    val viewModelFactory: ViewModelProvider.Factory = ViewModelFactoryInjector(dependencyContainer)
}
