package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import woowacourse.shopping.di.ShoppingApplication
import woowacourse.shopping.ui.cart.CartScreen
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.products.ProductsScreen

object ShoppingRoute {
    const val PRODUCTS = "products"
    const val CART = "cart"
}

@Composable
fun ShoppingNavHost(navController: NavHostController = rememberNavController()) {
    val application = LocalContext.current.applicationContext as ShoppingApplication
    NavHost(
        navController = navController,
        startDestination = ShoppingRoute.PRODUCTS,
    ) {
        composable(ShoppingRoute.PRODUCTS) {
            ProductsScreen(onNavigateToCart = { navController.navigate(ShoppingRoute.CART) })
        }
        composable(ShoppingRoute.CART) { backStackEntry ->
            val destinationScope =
                rememberNavigationDestinationScope(
                    backStackEntry = backStackEntry,
                    dependencyResolver = application.dependencyContainer,
                )
            val dateFormatter =
                remember(destinationScope) {
                    application.dependencyContainer.getInScope(DateFormatter::class, destinationScope)
                }
            CartScreen(
                dateFormatter = dateFormatter,
                onNavigateUp = { navController.navigateUp() },
            )
        }
    }
}
