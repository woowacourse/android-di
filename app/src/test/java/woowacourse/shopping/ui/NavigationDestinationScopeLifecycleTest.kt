package woowacourse.shopping.ui

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.di.dependencyContainer
import woowacourse.shopping.di.ShoppingScopes
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
class NavigationDestinationScopeLifecycleTest {
    @Test
    fun `화면 스코프는 구성 변경에 유지되고 pop과 재진입 때 새 DateFormatter를 만든다`() {
        val activity = Robolectric.buildActivity(ComponentActivity::class.java).setup().get()
        val container =
            dependencyContainer {
                registerFactory(DateFormatter::class, scope = ShoppingScopes.NAVIGATION_DESTINATION) {
                    DateFormatter(activity)
                }
            }
        val firstStore = ViewModelStore()
        val firstFactory = NavigationDestinationScopeViewModelFactory(container, "cart-entry-1")
        val firstViewModel = ViewModelProvider(firstStore, firstFactory)[NavigationDestinationScopeViewModel::class.java]
        val firstFormatter = container.getInScope(DateFormatter::class, firstViewModel.scope)
        val reusedFormatter = container.getInScope(DateFormatter::class, firstViewModel.scope)

        // The same ViewModelStore is retained while the Activity is recreated.
        val retainedViewModel =
            ViewModelProvider(
                firstStore,
                NavigationDestinationScopeViewModelFactory(container, "cart-entry-1"),
            )[NavigationDestinationScopeViewModel::class.java]

        assertThat(retainedViewModel).isSameInstanceAs(firstViewModel)
        assertThat(reusedFormatter).isSameInstanceAs(firstFormatter)
        assertThat(firstViewModel.scope.isClosed).isFalse()

        // Popping a NavBackStackEntry clears its ViewModelStore and closes its owned scope.
        firstStore.clear()

        assertThat(firstViewModel.scope.isClosed).isTrue()

        val reenteredStore = ViewModelStore()
        val reenteredViewModel =
            ViewModelProvider(
                reenteredStore,
                NavigationDestinationScopeViewModelFactory(container, "cart-entry-2"),
            )[NavigationDestinationScopeViewModel::class.java]
        val reenteredFormatter = container.getInScope(DateFormatter::class, reenteredViewModel.scope)

        assertThat(reenteredViewModel.scope.id).isEqualTo("cart-entry-2")
        assertThat(reenteredFormatter).isNotSameInstanceAs(firstFormatter)
        assertThat(firstViewModel.scope.isClosed).isTrue()
        reenteredStore.clear()
        activity.finish()
    }

    @Test
    fun `화면을 반복해서 나가도 이전 스코프가 모두 닫힌다`() {
        val container = dependencyContainer {}
        val previousScopes = mutableListOf<woowacourse.di.DependencyScope>()

        repeat(5) { index ->
            val store = ViewModelStore()
            val viewModel =
                ViewModelProvider(
                    store,
                    NavigationDestinationScopeViewModelFactory(container, "cart-entry-$index"),
                )[NavigationDestinationScopeViewModel::class.java]
            previousScopes += viewModel.scope

            store.clear()
        }

        assertThat(previousScopes).hasSize(5)
        assertThat(previousScopes.all { it.isClosed }).isTrue()
    }
}
