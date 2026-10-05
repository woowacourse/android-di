package woowacourse.shopping

import android.content.Context
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.common.truth.Truth.assertThat
import io.github.firstwoosun.di.DependencyBinding
import io.github.firstwoosun.di.DependencyContainer
import io.github.firstwoosun.di.InstanceProvider
import io.github.firstwoosun.di.ScopeKind
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.di.ScreenScopedViewModel
import woowacourse.shopping.ui.di.ScreenScopedViewModelFactory

@RunWith(RobolectricTestRunner::class)
class ScreenScopeNavigationTest {
    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var container: DependencyContainer
    private lateinit var navController: NavHostController

    private lateinit var latestOwner: ScreenScopedViewModel
    private lateinit var latestFormatter: DateFormatter

    // 테스트에서 장바구니 목적지의 재구성을 유도하는 값
    private val revision = mutableStateOf(0)
    private var renderedRevision = -1

    @Before
    fun setUp() {
        val applicationContext =
            RuntimeEnvironment.getApplication().applicationContext

        container =
            DependencyContainer(
                instanceProvider =
                    InstanceProvider { type ->
                        if (type == Context::class) {
                            applicationContext
                        } else {
                            null
                        }
                    },
                bindings =
                    listOf(
                        DependencyBinding(
                            type = DateFormatter::class,
                            implementation = DateFormatter::class,
                            scopeKind = ScopeKind.Screen,
                        ),
                    ),
            )
    }

    @After
    fun tearDown() {
        container.close()
    }

    @Test
    fun `장바구니 화면을 재구성해도 같은 DateFormatter를 사용한다`() {
        launchNavHost()
        enterCart()

        lateinit var firstOwner: ScreenScopedViewModel
        lateinit var firstFormatter: DateFormatter

        composeRule.runOnIdle {
            firstOwner = latestOwner
            firstFormatter = latestFormatter
            revision.value += 1
        }

        composeRule.waitForIdle()

        composeRule.runOnIdle {
            assertThat(renderedRevision).isEqualTo(1)
            assertThat(latestOwner).isSameInstanceAs(firstOwner)
            assertThat(latestFormatter).isSameInstanceAs(firstFormatter)
            assertThat(firstOwner.scope.isClosed).isFalse()
        }
    }

    @Test
    fun `화면이 가려져도 백스택에 남아 있으면 스코프를 유지한다`() {
        launchNavHost()
        enterCart()

        lateinit var cartOwner: ScreenScopedViewModel
        lateinit var cartFormatter: DateFormatter

        composeRule.runOnIdle {
            cartOwner = latestOwner
            cartFormatter = latestFormatter
            navController.navigate("other")
        }

        composeRule.waitForIdle()

        composeRule.runOnIdle {
            assertThat(cartOwner.scope.isClosed).isFalse()
            assertThat(navController.popBackStack()).isTrue()
        }

        composeRule.waitForIdle()

        composeRule.runOnIdle {
            assertThat(latestOwner).isSameInstanceAs(cartOwner)
            assertThat(latestFormatter).isSameInstanceAs(cartFormatter)
        }
    }

    @Test
    fun `장바구니를 pop하면 스코프가 닫히고 재진입하면 새 객체를 만든다`() {
        launchNavHost()
        enterCart()

        lateinit var firstOwner: ScreenScopedViewModel
        lateinit var firstFormatter: DateFormatter

        composeRule.runOnIdle {
            firstOwner = latestOwner
            firstFormatter = latestFormatter

            assertThat(navController.popBackStack()).isTrue()
        }

        // Navigation 전환이 완료되고 ViewModelStore가 정리되기를 기다린다.
        composeRule.waitForIdle()
        composeRule.waitUntil(timeoutMillis = 5_000) {
            firstOwner.scope.isClosed
        }

        enterCart()

        composeRule.runOnIdle {
            assertThat(firstOwner.scope.isClosed).isTrue()
            assertThat(latestOwner).isNotSameInstanceAs(firstOwner)
            assertThat(latestFormatter).isNotSameInstanceAs(firstFormatter)
            assertThat(latestOwner.scope.isClosed).isFalse()
        }
    }

    private fun launchNavHost() {
        composeRule.setContent {
            val controller = rememberNavController()
            val factory =
                remember(container) {
                    ScreenScopedViewModelFactory(container)
                }

            SideEffect {
                navController = controller
            }

            NavHost(
                navController = controller,
                startDestination = "products",
            ) {
                composable("products") {
                    Button(
                        onClick = { controller.navigate("cart") },
                    ) {
                        Text("장바구니")
                    }
                }

                composable("cart") { backStackEntry ->
                    val owner: ScreenScopedViewModel =
                        viewModel(
                            viewModelStoreOwner = backStackEntry,
                            factory = factory,
                        )

                    val formatter =
                        container.getInstance(
                            type = DateFormatter::class,
                            scope = owner.scope,
                        ) as DateFormatter

                    val currentRevision = revision.value

                    SideEffect {
                        latestOwner = owner
                        latestFormatter = formatter
                        renderedRevision = currentRevision
                    }

                    Text("장바구니 $currentRevision")
                }

                composable("other") {
                    Text("다른 화면")
                }
            }
        }

        composeRule.waitForIdle()
    }

    private fun enterCart() {
        composeRule.runOnIdle {
            navController.navigate("cart")
        }

        composeRule.waitForIdle()

        composeRule.runOnIdle {
            assertThat(navController.currentDestination?.route)
                .isEqualTo("cart")
            assertThat(latestOwner.scope.isClosed).isFalse()
        }
    }
}