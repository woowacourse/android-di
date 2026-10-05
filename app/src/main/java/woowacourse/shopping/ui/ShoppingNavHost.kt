package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.firstwoosun.di.DependencyContainer
import woowacourse.shopping.ui.cart.CartScreen
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.di.ScreenScopedViewModel
import woowacourse.shopping.ui.di.ScreenScopedViewModelFactory
import woowacourse.shopping.ui.di.ViewModelScopedViewModelFactory
import woowacourse.shopping.ui.products.ProductsScreen

object ShoppingRoute {
    const val PRODUCTS = "products"
    const val CART = "cart"
}

@Composable
fun ShoppingNavHost(
    viewModelScopedViewModelFactory: ViewModelScopedViewModelFactory,
    dependencyContainer: DependencyContainer,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = ShoppingRoute.PRODUCTS,
    ) {
        composable(ShoppingRoute.PRODUCTS) { backStackEntry ->
            ProductsScreen(
                onNavigateToCart = { navController.navigate(ShoppingRoute.CART) },
                viewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = viewModelScopedViewModelFactory
                ),
            )
        }
        composable(ShoppingRoute.CART) { backStackEntry ->
            val screenScopeFactory =
                remember(dependencyContainer) {
                    ScreenScopedViewModelFactory(dependencyContainer)
                }

            val screenScopeOwner: ScreenScopedViewModel =
                viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = screenScopeFactory,
                )

            val dateFormatter =
                dependencyContainer.getInstance(
                    type = DateFormatter::class,
                    scope = screenScopeOwner.scope,
                ) as DateFormatter

            CartScreen(
                onNavigateUp = { navController.navigateUp() },
                viewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = viewModelScopedViewModelFactory,
                ),
                dateFormatter = dateFormatter,
            )
        }
    }
}
