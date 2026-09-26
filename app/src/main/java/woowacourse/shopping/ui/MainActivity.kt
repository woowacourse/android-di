package woowacourse.shopping.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCart
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.RoomCart
import woowacourse.shopping.data.RoomCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DependencyContainer
import woowacourse.shopping.ui.theme.ShoppingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val database =
            Room
                .databaseBuilder(
                    applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping.db",
                ).build()

        val cartProductDao = database.cartProductDao()
        DependencyContainer.register(CartProductDao::class, cartProductDao)
        DependencyContainer.register(
            CartRepository::class,
            RoomCart::class,
            RoomCartRepository(cartProductDao),
        )
        DependencyContainer.register(
            CartRepository::class,
            InMemoryCart::class,
            InMemoryCartRepository(),
        )

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingTheme {
                ShoppingNavHost()
            }
        }
    }
}
