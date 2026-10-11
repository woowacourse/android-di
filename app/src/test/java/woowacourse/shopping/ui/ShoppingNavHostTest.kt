package woowacourse.shopping.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.di.DependencyScope
import woowacourse.di.KirbyDIContainer
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.KirbyViewModelFactory
import woowacourse.shopping.di.LocalDIContainer
import woowacourse.shopping.di.NavEntryScopeViewModel
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.products.ProductsViewModel
import woowacourse.shopping.ui.theme.ShoppingTheme

@RunWith(RobolectricTestRunner::class)
class ShoppingNavHostTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: NavHostController

    @Test
    fun `장바구니 엔트리는 포맷터를 재사용하고 제거 후 재진입하면 새 포맷터를 받는다`() {
        val container = showNavHost()
        val productsEntry = composeRule.runOnIdle { requireNotNull(navController.currentBackStackEntry) }
        val productsViewModel = composeRule.runOnIdle { ViewModelProvider(productsEntry)[ProductsViewModel::class.java] }
        val productsScope = requireNotNull(productsViewModel.getCloseable<DependencyScope>(KirbyViewModelFactory.SCOPE_KEY))
        val productRepository = container.resolve(ProductRepository::class, scope = productsScope)
        navigateToCart()
        assertThat(container.resolve(ProductRepository::class, scope = productsScope)).isSameAs(productRepository)
        val firstScope = currentEntryScope()
        val firstFormatter = container.resolve(DateFormatter::class, scope = firstScope)

        navigateToCart()
        val secondScope = currentEntryScope()
        val secondFormatter = container.resolve(DateFormatter::class, scope = secondScope)

        assertThat(secondScope).isNotSameAs(firstScope)
        assertThat(secondFormatter).isNotSameAs(firstFormatter)
        assertThat(container.resolve(DateFormatter::class, scope = firstScope)).isSameAs(firstFormatter)

        popEntry()

        assertThat(currentEntryScope()).isSameAs(firstScope)
        assertThat(container.resolve(DateFormatter::class, scope = firstScope)).isSameAs(firstFormatter)
        assertThatThrownBy { container.resolve(DateFormatter::class, scope = secondScope) }
            .hasMessageContaining("종료된 스코프")

        popEntry()

        assertThatThrownBy { container.resolve(DateFormatter::class, scope = firstScope) }
            .hasMessageContaining("종료된 스코프")
        val returnedViewModel =
            composeRule.runOnIdle {
                ViewModelProvider(requireNotNull(navController.currentBackStackEntry))[ProductsViewModel::class.java]
            }
        assertThat(returnedViewModel).isSameAs(productsViewModel)
        assertThat(container.resolve(ProductRepository::class, scope = productsScope)).isSameAs(productRepository)
        var previousFormatter = firstFormatter
        repeat(10) {
            navigateToCart()
            val scope = currentEntryScope()
            val formatter = container.resolve(DateFormatter::class, scope = scope)
            assertThat(formatter).isNotSameAs(previousFormatter)
            popEntry()
            assertThatThrownBy { container.resolve(DateFormatter::class, scope = scope) }
                .hasMessageContaining("종료된 스코프")
            previousFormatter = formatter
        }
    }

    @Test
    fun `장바구니 엔트리는 Activity 재생성 후에도 같은 포맷터를 유지한다`() {
        val container = showNavHost()
        navigateToCart()
        val scope = currentEntryScope()
        val formatter = container.resolve(DateFormatter::class, scope = scope)
        val activity = composeRule.activity

        recreateActivity()

        assertThat(composeRule.activity).isNotSameAs(activity)
        assertThat(currentEntryScope()).isSameAs(scope)
        assertThat(container.resolve(DateFormatter::class, scope = currentEntryScope())).isSameAs(formatter)

        popEntry()

        assertThatThrownBy { container.resolve(DateFormatter::class, scope = scope) }
            .hasMessageContaining("종료된 스코프")
    }

    private fun showNavHost(): KirbyDIContainer {
        val container = (RuntimeEnvironment.getApplication() as ShoppingApplication).container
        val activity = composeRule.activity
        composeRule.runOnUiThread {
            activity.setContent {
                CompositionLocalProvider(LocalDIContainer provides container) {
                    navController = rememberNavController()
                    ShoppingTheme {
                        ShoppingNavHost(navController)
                    }
                }
            }
        }
        composeRule.waitForIdle()
        return container
    }

    private fun recreateActivity() {
        composeRule.activityRule.scenario.recreate()
        showNavHost()
    }

    private fun navigateToCart() {
        composeRule.runOnIdle { navController.navigate(ShoppingRoute.CART) }
        composeRule.waitForIdle()
    }

    private fun popEntry() {
        composeRule.runOnIdle { assertThat(navController.popBackStack()).isTrue() }
        composeRule.waitForIdle()
    }

    private fun currentEntryScope(): DependencyScope =
        composeRule.runOnIdle {
            val entry = requireNotNull(navController.currentBackStackEntry)
            ViewModelProvider(entry)[NavEntryScopeViewModel::class.java].scope
        }
}
