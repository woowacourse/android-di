package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import woowacourse.shopping.di.HunnitFactory
import woowacourse.shopping.di.ScreenScopeOwner
import woowacourse.shopping.ui.cart.CartScreen
import woowacourse.shopping.ui.cart.DateFormatter
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
        composable(ShoppingRoute.PRODUCTS) { backStackEntry ->
            val scopeOwner: ScreenScopeOwner =
                viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = HunnitFactory.screenScopeFactory(),
                )
            ProductsScreen(
                onNavigateToCart = { navController.navigate(ShoppingRoute.CART) },
                factory = HunnitFactory.viewModelFactory(scopeOwner.scope),
            )
        }
        composable(ShoppingRoute.CART) { backStackEntry ->
            val scopeOwner: ScreenScopeOwner =
                viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = HunnitFactory.screenScopeFactory(),
                )
            val dateFormatter =
                remember(scopeOwner.scope) {
                    scopeOwner.scope.resolve(DateFormatter::class) as DateFormatter
                }
            CartScreen(
                onNavigateUp = { navController.navigateUp() },
                dateFormatter = dateFormatter,
                factory = HunnitFactory.viewModelFactory(scopeOwner.scope),
            )
        }
    }
}
