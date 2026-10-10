package woowacourse.shopping.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.di.DiViewModelFactory
import woowacourse.shopping.ui.theme.ShoppingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val diManager = (application as ShoppingApplication).diManager
        val applicationKey = (application as ShoppingApplication).applicationKey
        val viewModelFactory =
            DiViewModelFactory(
                diManager = diManager,
                parentKey = applicationKey,
            )

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingTheme {
                ShoppingNavHost(
                    viewModelFactory = viewModelFactory,
                    diManager = diManager,
                    applicationKey = applicationKey,
                )
            }
        }
    }
}
