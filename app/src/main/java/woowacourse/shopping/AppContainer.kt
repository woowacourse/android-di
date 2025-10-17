package woowacourse.shopping

import android.content.Context
import woowacourse.peto.di.DependencyContainer
import woowacourse.peto.di.DependencyModule
import woowacourse.shopping.data.db.DatabaseModule
import woowacourse.shopping.data.db.ShoppingDatabase
import woowacourse.shopping.data.repository.RepositoryModule
import woowacourse.shopping.ui.cart.DateFormatterModule

class AppContainer(
    context: Context,
) {
    private val shoppingDatabase: ShoppingDatabase = ShoppingDatabase.getInstance(context)
    private val databaseModule = DatabaseModule(shoppingDatabase)
    private val repositoryModule: DependencyModule = RepositoryModule(databaseModule)
    private val dataFormatterModule = DateFormatterModule(context)

    val dependencyContainer: DependencyContainer =
        DependencyContainer(
            listOf(
                databaseModule,
                repositoryModule,
                dataFormatterModule,
            ),
        )
}
