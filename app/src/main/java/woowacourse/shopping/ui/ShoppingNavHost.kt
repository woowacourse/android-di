package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import woowacourse.di.get
import woowacourse.shopping.di.rememberScreenScope
import woowacourse.shopping.ui.cart.CartScreen
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
        composable(ShoppingRoute.CART) { entry ->
            val scope = rememberScreenScope(entry)
            CartScreen(dateFormatter = scope.get(), onNavigateUp = { navController.navigateUp() })
        }
    }
}
