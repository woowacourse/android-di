package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.ui.cart.CartScreen
import woowacourse.shopping.ui.cart.CartScreenScopeViewModel
import woowacourse.shopping.ui.cart.CartScreenScopeViewModelFactory
import woowacourse.shopping.ui.cart.DIContainerKey
import woowacourse.shopping.ui.cart.ScreenEntryIdKey
import woowacourse.shopping.ui.products.ProductsScreen

object ShoppingRoute {
    const val PRODUCTS = "products"
    const val CART = "cart"
}

@Composable
fun ShoppingNavHost(
    diContainer: DIContainer,
    navController: NavHostController = rememberNavController(),
) {
    val viewModelFactory = remember(diContainer) { ViewModelFactory(diContainer) }

    NavHost(
        navController = navController,
        startDestination = ShoppingRoute.PRODUCTS,
    ) {
        composable(ShoppingRoute.PRODUCTS) {
            ProductsScreen(
                onNavigateToCart = { navController.navigate(ShoppingRoute.CART) },
                viewModel = viewModel(factory = viewModelFactory),
            )
        }
        composable(ShoppingRoute.CART) { backStackEntry ->
            val extras =
                MutableCreationExtras(backStackEntry.defaultViewModelCreationExtras).apply {
                    this[ScreenEntryIdKey] = backStackEntry.id
                    this[DIContainerKey] = diContainer
                }

            val cartScopeViewModel =
                viewModel<CartScreenScopeViewModel>(
                    viewModelStoreOwner = backStackEntry,
                    factory = CartScreenScopeViewModelFactory,
                    extras = extras,
                )

            CartScreen(
                onNavigateUp = { navController.navigateUp() },
                dateFormatter = cartScopeViewModel.dateFormatter,
                viewModel = viewModel(factory = viewModelFactory),
            )
        }
    }
}
