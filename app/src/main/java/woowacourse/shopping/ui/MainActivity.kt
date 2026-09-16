package woowacourse.shopping.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import woowacourse.shopping.Storage
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel
import woowacourse.shopping.ui.theme.ShoppingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val productsViewModel = ProductsViewModel(Storage.productRepository, Storage.cartRepository)
        val cartViewModel = CartViewModel(Storage.cartRepository)

        setContent {
            ShoppingTheme {
                ShoppingNavHost(
                    productsViewModel,
                    cartViewModel
                )
            }
        }
    }
}
