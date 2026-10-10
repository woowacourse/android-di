package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import woowacourse.shopping.di.LocalDIContainer
import woowacourse.shopping.di.NavEntryScopeViewModel
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
            ProductsScreen(onNavigateToCart = { navController.navigate(ShoppingRoute.CART) })
        }
        composable(ShoppingRoute.CART) { entry ->
            val container = LocalDIContainer.current
            val scopeOwner: NavEntryScopeViewModel =
                viewModel(
                    viewModelStoreOwner = entry,
                    factory = NavEntryScopeViewModel.factory(container),
                )
            val dateFormatter = container.resolve(DateFormatter::class, scope = scopeOwner.scope)

            CartScreen(
                onNavigateUp = { navController.navigateUp() },
                dateFormatter = dateFormatter,
            )
        }
    }
}
