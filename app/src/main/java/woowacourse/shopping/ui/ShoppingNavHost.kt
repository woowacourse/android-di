package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import woowacourse.shopping.ui.cart.CartScreen
import woowacourse.shopping.ui.cart.CartScreenScopeFactory
import woowacourse.shopping.ui.cart.CartScreenScopeViewModel
import woowacourse.shopping.ui.products.ProductsScreen

object ShoppingRoute {
    const val PRODUCTS = "products"
    const val CART = "cart"
}

@Composable
fun ShoppingNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = ShoppingRoute.PRODUCTS,
    ) {
        composable(ShoppingRoute.PRODUCTS) {
            ProductsScreen(onNavigateToCart = { navController.navigate(ShoppingRoute.CART) })
        }
        composable(ShoppingRoute.CART) { backStackEntry ->
            val factory = remember(backStackEntry.id) { CartScreenScopeFactory(backStackEntry.id) }
            val screenScope =
                viewModel<CartScreenScopeViewModel>(
                    viewModelStoreOwner = backStackEntry,
                    factory = factory,
                )
            CartScreen(
                onNavigateUp = { navController.navigateUp() },
                dateFormatter = screenScope.dateFormatter,
            )
        }
    }
}
