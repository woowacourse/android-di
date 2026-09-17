package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import woowacourse.shopping.ui.cart.CartScreen
import woowacourse.shopping.ui.di.CommonViewModelFactory
import woowacourse.shopping.ui.products.ProductsScreen

object ShoppingRoute {
    const val PRODUCTS = "products"
    const val CART = "cart"
}

@Composable
fun ShoppingNavHost(
    viewModelFactory: CommonViewModelFactory,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = ShoppingRoute.PRODUCTS,
    ) {
        composable(ShoppingRoute.PRODUCTS) {
            ProductsScreen(
                onNavigateToCart = { navController.navigate(ShoppingRoute.CART) },
                viewModel = viewModel(factory = viewModelFactory)
            )
        }
        composable(ShoppingRoute.CART) {
            CartScreen(onNavigateUp = { navController.navigateUp() },
                viewModel = viewModel(factory = viewModelFactory)
            )
        }
    }
}
