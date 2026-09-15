package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import woowacourse.shopping.ui.cart.CartScreen
import woowacourse.shopping.ui.products.ProductsScreen
import woowacourse.shopping.ui.products.ProductsViewModel

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
            val viewModel: ProductsViewModel = viewModel(factory = ProductsViewModel.Factory)
            ProductsScreen(
                onNavigateToCart = { navController.navigate(ShoppingRoute.CART) },
                viewModel = viewModel,
            )
        }
        composable(ShoppingRoute.CART) {
            CartScreen(onNavigateUp = { navController.navigateUp() })
        }
    }
}
