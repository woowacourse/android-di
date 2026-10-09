package woowacourse.shopping.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import woowacourse.shopping.di.AoDi
import woowacourse.shopping.ui.theme.ShoppingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingTheme {
                ShoppingNavHost(
                    viewModelFactory = AoDi,
                    cartScreenScopeFactory = AoDi,
                )
            }
        }
    }
}
