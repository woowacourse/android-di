package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import woowacourse.shopping.ui.cart.CartScreen
import woowacourse.shopping.ui.cart.CartScreenScopeFactory
import woowacourse.shopping.ui.cart.CartScreenScopeViewModel
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsScreen
import woowacourse.shopping.ui.products.ProductsViewModel

object ShoppingRoute {
    const val PRODUCTS = "products"
    const val CART = "cart"
}

@Composable
internal fun ShoppingNavHost(
    viewModelFactory: ViewModelProvider.Factory,
    cartScreenScopeFactory: CartScreenScopeFactory,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = ShoppingRoute.PRODUCTS,
    ) {
        composable(ShoppingRoute.PRODUCTS) { backStackEntry ->
            ProductsScreen(
                viewModel =
                    viewModel<ProductsViewModel>(
                        viewModelStoreOwner = backStackEntry,
                        factory = viewModelFactory,
                    ),
                onNavigateToCart = { navController.navigate(ShoppingRoute.CART) },
            )
        }
        composable(ShoppingRoute.CART) { backStackEntry ->
            CartRoute(
                backStackEntry = backStackEntry,
                viewModelFactory = viewModelFactory,
                screenScopeFactory = cartScreenScopeFactory,
                onNavigateUp = { navController.navigateUp() },
            )
        }
    }
}

@Composable
private fun CartRoute(
    backStackEntry: NavBackStackEntry,
    viewModelFactory: ViewModelProvider.Factory,
    screenScopeFactory: CartScreenScopeFactory,
    onNavigateUp: () -> Unit,
) {
    val scopeFactory =
        remember(backStackEntry.id, screenScopeFactory) {
            screenScopeFactory.factoryFor(backStackEntry.id)
        }
    val screenScope =
        viewModel<CartScreenScopeViewModel>(
            viewModelStoreOwner = backStackEntry,
            factory = scopeFactory,
        )
    val cartViewModel =
        viewModel<CartViewModel>(
            viewModelStoreOwner = backStackEntry,
            factory = viewModelFactory,
        )

    CartScreen(
        viewModel = cartViewModel,
        dateFormatter = screenScope.dateFormatter,
        onNavigateUp = onNavigateUp,
    )
}
