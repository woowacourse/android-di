package woowacourse.shopping.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import woowacourse.di.DependencyContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.qualifier.InMemory
import woowacourse.shopping.ui.theme.ShoppingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database =
            Room
                .databaseBuilder(
                    applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping.db",
                ).build()

        val dao = database.cartProductDao()

        DependencyContainer.register(
            CartProductDao::class,
            dao,
        )

        DependencyContainer.register(
            CartRepository::class,
            woowacourse.shopping.di.qualifier.Room::class,
            DefaultCartRepository(dao),
        )

        DependencyContainer.register(
            CartRepository::class,
            InMemory::class,
            InMemoryCartRepository(),
        )

        enableEdgeToEdge()
        setContent {
            ShoppingTheme {
                ShoppingNavHost()
            }
        }
    }
}
