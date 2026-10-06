package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import woowacourse.shopping.di.AppDI
import woowacourse.shopping.di.ScreenScopeViewModel
import woowacourse.shopping.ui.cart.CartScreen
import woowacourse.shopping.ui.cart.CartViewModel
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
        composable(ShoppingRoute.PRODUCTS) {
            ProductsScreen(onNavigateToCart = { navController.navigate(ShoppingRoute.CART) })
        }
        composable(ShoppingRoute.CART) { entry ->
            val screen: ScreenScopeViewModel = viewModel(viewModelStoreOwner = entry)
            val cartViewModel: CartViewModel =
                viewModel(viewModelStoreOwner = entry, factory = AppDI.viewModelFactory(screen.scope))
            CartScreen(
                onNavigateUp = { navController.navigateUp() },
                dateFormatter = screen.scope.get(DateFormatter::class),
                viewModel = cartViewModel,
            )
        }
    }
}
