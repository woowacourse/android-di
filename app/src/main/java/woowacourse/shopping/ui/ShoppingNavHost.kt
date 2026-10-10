package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import woowacourse.shopping.MyApplication
import woowacourse.shopping.ui.cart.CartScreen
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.cart.ScreenScopeViewModel
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
            val container = (LocalContext.current.applicationContext as MyApplication).appContainer
            val owner: ScreenScopeViewModel =
                viewModel(
                    viewModelStoreOwner = entry,
                    factory =
                        object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T = ScreenScopeViewModel(container.appScope) as T
                        },
                )
            val dateFormatter = container.di.resolve(DateFormatter::class, scope = owner.scope) as DateFormatter
            CartScreen(
                dateFormatter = dateFormatter,
                onNavigateUp = { navController.navigateUp() },
            )
        }
    }
}
