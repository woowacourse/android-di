package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import woowacourse.shopping.MyApplication
import woowacourse.shopping.ViewModelFactory
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
            val container = (LocalContext.current.applicationContext as MyApplication).appContainer
            val cartViewModel: CartViewModel =
                viewModel(
                    viewModelStoreOwner = entry,
                    factory = ViewModelFactory.viewModelFactory(LocalContext.current),
                )
            val dateFormatter = container.di.resolve(
                DateFormatter::class,
                scope = cartViewModel.screenScope
            ) as DateFormatter
            CartScreen(
                dateFormatter = dateFormatter,
                viewModel = cartViewModel,
                onNavigateUp = { navController.navigateUp() },
            )
        }
    }
}
