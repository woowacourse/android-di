package woowacourse.shopping.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.ui.di.CommonViewModelFactory
import woowacourse.shopping.ui.theme.ShoppingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as ShoppingApplication

        enableEdgeToEdge()
        setContent {
            ShoppingTheme {
                ShoppingNavHost(
                    viewModelFactory = CommonViewModelFactory(app.repositoryContainer)
                )
            }
        }
    }
}
