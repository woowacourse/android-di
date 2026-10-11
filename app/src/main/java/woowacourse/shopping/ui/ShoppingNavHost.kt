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
import woowacourse.shopping.di.DependencyKey
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
        composable(ShoppingRoute.PRODUCTS) {
            ProductsScreen(
                onNavigateToCart = { navController.navigate(ShoppingRoute.CART) },
                viewModel = viewModel(factory = ViewModelFactory),
            )
        }
        composable(ShoppingRoute.CART) { backStackEntry ->
            val extras =
                MutableCreationExtras(backStackEntry.defaultViewModelCreationExtras).apply {
                    this[ScreenEntryIdKey] = backStackEntry.id
                }
            val screenScopeViewModel =
                viewModel<ScreenScopeViewModel>(
                    viewModelStoreOwner = backStackEntry,
                    factory = ScreenScopeViewModelFactory,
                    extras = extras,
                )
            val dateFormatter =
                remember(screenScopeViewModel.scopeKey) {
                    DIContainer.createInstance(
                        DependencyKey(DateFormatter::class),
                        screenScopeViewModel.scopeKey,
                    )
                }

            CartScreen(
                onNavigateUp = { navController.navigateUp() },
                dateFormatter = dateFormatter,
                viewModel = viewModel(factory = ViewModelFactory),
            )
        }
    }
}
