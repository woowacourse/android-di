package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import woowacourse.shopping.ui.cart.CartScreen
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsScreen
import woowacourse.shopping.ui.products.ProductsViewModel

object ShoppingRoute {
    const val PRODUCTS = "products"
    const val CART = "cart"
}

@Composable
fun ShoppingNavHost(
    productsViewModel: ProductsViewModel,
    cartViewModel: CartViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ShoppingRoute.PRODUCTS,
    ) {
        composable(ShoppingRoute.PRODUCTS) {
            ProductsScreen(viewModel = productsViewModel, onNavigateToCart = { navController.navigate(ShoppingRoute.CART) })
        }
        composable(ShoppingRoute.CART) {
            CartScreen(viewModel = cartViewModel, onNavigateUp = { navController.navigateUp() })
        }
    }
}
