package woowacourse.shopping.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.di.LocalDIContainer
import woowacourse.shopping.ui.theme.ShoppingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as ShoppingApplication).container
        setContent {
            CompositionLocalProvider(LocalDIContainer provides container) {
                ShoppingTheme {
                    ShoppingNavHost()
                }
            }
        }
    }
}
