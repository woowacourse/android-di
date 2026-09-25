package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import io.github.firstwoosun.di.DependencyBinding
import io.github.firstwoosun.di.DependencyContainer
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.di.DataContainer
import woowacourse.shopping.data.di.InMemory
import woowacourse.shopping.data.di.RoomBacked
import woowacourse.shopping.ui.di.CommonViewModelFactory

class ShoppingApplication : Application() {
    private lateinit var dataContainer: DataContainer
    private lateinit var dependencyContainer: DependencyContainer
    lateinit var viewModelFactory: CommonViewModelFactory

    override fun onCreate() {
        super.onCreate()
        val database =
            Room
                .databaseBuilder(
                    applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping_db",
                ).build()

        dataContainer = DataContainer.create(database)
        dependencyContainer =
            DependencyContainer(
                instanceProvider = dataContainer,
                bindings =
                    listOf(
                        DependencyBinding(
                            type = CartRepository::class,
                            implementation = DefaultCartRepository::class,
                            qualifier = RoomBacked::class,
                        ),
                        DependencyBinding(
                            type = ProductRepository::class,
                            implementation = ProductRepository::class,
                            qualifier = InMemory::class,
                        ),
                    ),
            )
        viewModelFactory = CommonViewModelFactory(dependencyContainer)
    }
}
