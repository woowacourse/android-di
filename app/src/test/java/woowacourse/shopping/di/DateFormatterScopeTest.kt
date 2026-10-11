package woowacourse.shopping.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.di.DependencyContainer
import woowacourse.di.Scope
import woowacourse.di.ScopeType
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
class DateFormatterScopeTest {
    private class FakeCartProductDao : CartProductDao {
        override suspend fun getAll(): List<CartProductEntity> = emptyList()

        override suspend fun insert(cartProduct: CartProductEntity) = Unit

        override suspend fun delete(id: Long) = Unit
    }

    @Test
    fun `같은 화면 스코프에서 DateFormatter를 재사용하고 다른 화면 스코프와 분리한다`() {
        val container = DependencyContainer()
        val screen = ScopeType("screen")
        val context = RuntimeEnvironment.getApplication()
        var creationCount = 0
        container.bind(DateFormatter::class, screen) {
            creationCount++
            DateFormatter(context)
        }
        val firstScope = container.openScope(screen, "cart-entry-1")
        val secondScope = container.openScope(screen, "cart-entry-2")

        val firstFormatter = firstScope.resolve(DateFormatter::class)
        val secondFormatter = secondScope.resolve(DateFormatter::class)

        assertThat(firstScope.resolve(DateFormatter::class)).isSameAs(firstFormatter)
        assertThat(secondScope.resolve(DateFormatter::class)).isSameAs(secondFormatter)
        assertThat(container.openScope(screen, "cart-entry-1").resolve(DateFormatter::class))
            .isSameAs(firstFormatter)
        assertThat(firstFormatter).isNotSameAs(secondFormatter)
        assertThat(creationCount).isEqualTo(2)
    }

    @Test
    fun `같은 엔트리의 화면 스코프는 유지하고 같은 경로의 다른 엔트리와 분리한다`() {
        val context = RuntimeEnvironment.getApplication()
        val activityController = Robolectric.buildActivity(ComponentActivity::class.java).setup()
        val activity = activityController.get()
        val navController = NavHostController(context)
        navController.setLifecycleOwner(activity)
        navController.setViewModelStore(activity.viewModelStore)
        navController.navigatorProvider.addNavigator(ComposeNavigator())
        navController.graph =
            navController.createGraph(startDestination = "cart") {
                composable("cart") {}
            }
        val container = DependencyContainer()
        val app = ScopeType("app")
        val screen = ScopeType("screen")
        container.bind(DateFormatter::class, screen) { DateFormatter(context) }
        val appScope = container.openScope(app, "application")
        val provider = ScreenScopeProvider(container, appScope, screen)
        val firstEntry = requireNotNull(navController.currentBackStackEntry)
        val firstScope = provider.get(firstEntry)
        val firstFormatter = firstScope.resolve(DateFormatter::class)

        navController.navigate("cart")
        val secondEntry = requireNotNull(navController.currentBackStackEntry)
        val secondScope = provider.get(secondEntry)

        assertThat(firstEntry.destination.route).isEqualTo(secondEntry.destination.route)
        assertThat(firstEntry.id).isNotEqualTo(secondEntry.id)
        assertThat(provider.get(firstEntry)).isSameAs(firstScope)
        assertThat(provider.get(firstEntry).resolve(DateFormatter::class)).isSameAs(firstFormatter)
        assertThat(provider.get(secondEntry)).isSameAs(secondScope)
        assertThat(secondScope).isNotSameAs(firstScope)
        assertThat(secondScope.resolve(DateFormatter::class)).isNotSameAs(firstFormatter)
        assertThat(container.activeScopeCount).isEqualTo(3)
        activityController.pause().stop().destroy()
    }

    @Test
    fun `AppDI의 엔트리 화면 스코프에서 DateFormatter를 조회한다`() {
        val context = RuntimeEnvironment.getApplication()
        val activityController = Robolectric.buildActivity(ComponentActivity::class.java).setup()
        val activity = activityController.get()
        val navController = NavHostController(context)
        navController.setLifecycleOwner(activity)
        navController.setViewModelStore(activity.viewModelStore)
        navController.navigatorProvider.addNavigator(ComposeNavigator())
        navController.graph =
            navController.createGraph(startDestination = "cart") {
                composable("cart") {}
            }
        val entry = requireNotNull(navController.currentBackStackEntry)
        val scope = AppDI.screenScopeProvider.get(entry)

        val formatter = scope.resolve(DateFormatter::class)

        assertThat(AppDI.screenScopeProvider.get(entry).resolve(DateFormatter::class)).isSameAs(formatter)
        activityController.pause().stop().destroy()
    }

    @Test
    fun `백스택에 남은 화면의 포매터는 유지하고 제거 후 재진입하면 새로 생성한다`() {
        val context = RuntimeEnvironment.getApplication()
        val activityController = Robolectric.buildActivity(ComponentActivity::class.java).setup()
        val activity = activityController.get()
        val navigator = ComposeNavigator()
        val navController = NavHostController(context)
        navController.setLifecycleOwner(activity)
        navController.setViewModelStore(activity.viewModelStore)
        navController.navigatorProvider.addNavigator(navigator)
        navController.graph =
            navController.createGraph(startDestination = "products") {
                composable("products") {}
                composable("cart") {}
                composable("details") {}
            }
        val productsEntry = requireNotNull(navController.currentBackStackEntry)
        navigator.onTransitionComplete(productsEntry)
        val container = DependencyContainer()
        val app = ScopeType("app")
        val screen = ScopeType("screen")
        container.bind(DateFormatter::class, screen) { DateFormatter(context) }
        val appScope = container.openScope(app, "application")
        val provider = ScreenScopeProvider(container, appScope, screen)

        navController.navigate("cart")
        val cartEntry = requireNotNull(navController.currentBackStackEntry)
        navigator.onTransitionComplete(productsEntry)
        navigator.onTransitionComplete(cartEntry)
        val cartScope = provider.get(cartEntry)
        val formatter = cartScope.resolve(DateFormatter::class)

        navController.navigate("details")
        val detailsEntry = requireNotNull(navController.currentBackStackEntry)
        navigator.onTransitionComplete(cartEntry)
        navigator.onTransitionComplete(detailsEntry)

        assertThat(cartEntry.lifecycle.currentState).isEqualTo(Lifecycle.State.CREATED)
        assertThat(provider.get(cartEntry)).isSameAs(cartScope)
        assertThat(cartScope.resolve(DateFormatter::class)).isSameAs(formatter)
        assertThat(container.activeScopeCount).isEqualTo(2)

        assertThat(navController.popBackStack()).isTrue()
        navigator.onTransitionComplete(detailsEntry)
        navigator.onTransitionComplete(cartEntry)
        val returnedEntry = requireNotNull(navController.currentBackStackEntry)
        assertThat(provider.get(returnedEntry).resolve(DateFormatter::class)).isSameAs(formatter)

        assertThat(navController.popBackStack()).isTrue()
        navigator.onTransitionComplete(cartEntry)
        navigator.onTransitionComplete(productsEntry)

        assertThat(cartEntry.lifecycle.currentState).isEqualTo(Lifecycle.State.DESTROYED)
        assertThat(container.activeScopeCount).isEqualTo(1)
        assertThatThrownBy {
            cartScope.resolve(DateFormatter::class)
        }.isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("닫힌 스코프")

        navController.navigate("cart")
        val newEntry = requireNotNull(navController.currentBackStackEntry)
        navigator.onTransitionComplete(productsEntry)
        navigator.onTransitionComplete(newEntry)
        val newScope = provider.get(newEntry)

        assertThat(newEntry.id).isNotEqualTo(cartEntry.id)
        assertThat(newScope).isNotSameAs(cartScope)
        assertThat(newScope.resolve(DateFormatter::class)).isNotSameAs(formatter)
        assertThat(container.activeScopeCount).isEqualTo(2)
        assertThat(container.openScope(app, "application")).isSameAs(appScope)
        activityController.pause().stop().destroy()
        assertThat(container.activeScopeCount).isEqualTo(1)
    }

    @Test
    fun `화면 진입과 이탈을 반복해도 화면과 ViewModel 스코프가 누적되지 않는다`() {
        val context = RuntimeEnvironment.getApplication()
        val activityController = Robolectric.buildActivity(ComponentActivity::class.java).setup()
        val activity = activityController.get()
        val navigator = ComposeNavigator()
        val navController = NavHostController(context)
        navController.setLifecycleOwner(activity)
        navController.setViewModelStore(activity.viewModelStore)
        navController.navigatorProvider.addNavigator(navigator)
        navController.graph =
            navController.createGraph(startDestination = "products") {
                composable("products") {}
                composable("cart") {}
            }
        val productsEntry = requireNotNull(navController.currentBackStackEntry)
        navigator.onTransitionComplete(productsEntry)
        val container = AppDI.createContainer(FakeCartProductDao())
        var creationCount = 0
        container.bind(DateFormatter::class, AppDI.screenScopeType) {
            creationCount++
            DateFormatter(context)
        }
        val appScope = container.openScope(AppDI.appScopeType, "application")
        val appRepository = appScope.resolve(CartRepository::class, RoomCart::class)
        val provider = ScreenScopeProvider(container, appScope, AppDI.screenScopeType)
        val factory = ReflectionViewModelFactory(container, appScope, AppDI.viewModelScopeType)
        val entryIds = mutableSetOf<String>()
        var previousFormatter: DateFormatter? = null

        repeat(10) {
            navController.navigate("cart")
            val entry = requireNotNull(navController.currentBackStackEntry)
            navigator.onTransitionComplete(productsEntry)
            navigator.onTransitionComplete(entry)
            val screenScope = provider.get(entry)
            val formatter = screenScope.resolve(DateFormatter::class)
            val viewModel = ViewModelProvider(entry, factory)[CartViewModel::class.java]
            val vmScope = requireNotNull(viewModel.getCloseable<Scope>(ReflectionViewModelFactory.SCOPE_KEY))
            assertThat(entryIds.add(entry.id)).isTrue()
            assertThat(formatter).isNotSameAs(previousFormatter)
            assertThat(viewModel.cartRepository).isSameAs(appRepository)
            assertThat(container.activeScopeCount).isEqualTo(3)

            assertThat(navController.popBackStack()).isTrue()
            navigator.onTransitionComplete(entry)
            navigator.onTransitionComplete(productsEntry)

            assertThat(container.activeScopeCount).isEqualTo(1)
            assertThatThrownBy {
                screenScope.resolve(DateFormatter::class)
            }.isInstanceOf(IllegalStateException::class.java)
                .hasMessageContaining("닫힌 스코프")
            assertThatThrownBy {
                vmScope.resolve(CartRepository::class, RoomCart::class)
            }.isInstanceOf(IllegalStateException::class.java)
                .hasMessageContaining("닫힌 스코프")
            assertThat(appScope.resolve(CartRepository::class, RoomCart::class)).isSameAs(appRepository)
            previousFormatter = formatter
        }

        assertThat(creationCount).isEqualTo(10)
        activityController.pause().stop().destroy()
        container.close()
        assertThat(container.activeScopeCount).isZero()
    }
}
