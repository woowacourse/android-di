package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.cart.CartScreen
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsScreen
import woowacourse.shopping.ui.products.ProductsViewModel

object ShoppingRoute {
    const val PRODUCTS = "products"
    const val CART = "cart"
}

@Composable
fun ShoppingNavHost(navController: NavHostController = rememberNavController()) {
    val productsViewModel: ProductsViewModel =
        viewModel(
            factory =
                viewModelFactory {
                    initializer {
                        ProductsViewModel(
                            productRepository = ProductRepository(),
                            cartRepository = CartRepository(),
                        )
                    }
                },
        )
    val cartViewModel: CartViewModel =
        viewModel(
            factory =
                viewModelFactory {
                    initializer {
                        CartViewModel(cartRepository = CartRepository())
                    }
                },
        )

    NavHost(
        navController = navController,
        startDestination = ShoppingRoute.PRODUCTS,
    ) {
        composable(ShoppingRoute.PRODUCTS) {
            ProductsScreen(
                onNavigateToCart = { navController.navigate(ShoppingRoute.CART) },
                viewModel = productsViewModel,
            )
        }
        composable(ShoppingRoute.CART) {
            CartScreen(
                onNavigateUp = { navController.navigateUp() },
                viewModel = cartViewModel,
            )
        }
    }
}
