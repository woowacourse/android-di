package woowacourse.shopping.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.HunnitFactory
import woowacourse.shopping.ui.theme.ShoppingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database =
            Room.databaseBuilder(applicationContext, ShoppingDatabase::class.java, "shopping.db").build()
        HunnitFactory.register(CartProductDao::class, database.cartProductDao())
        setContent {
            ShoppingTheme {
                ShoppingNavHost()
            }
        }
    }
}
