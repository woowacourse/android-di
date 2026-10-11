package woowacourse.shopping.ui.cart

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.google.common.truth.Truth.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.LocalMemoryCart
import woowacourse.shopping.di.ShoppingScopes

@RunWith(RobolectricTestRunner::class)
class CartScreenScopeTest {
    @Test
    fun `포맷터 생성 실패 시 캐시를 비우고 기존 화면은 유지하며 재시도할 수 있다`() {
        val application = RuntimeEnvironment.getApplication() as ShoppingApplication
        val container = application.container.diContainer
        val extras = MutableCreationExtras().apply { this[APPLICATION_KEY] = application }
        val healthyStore = ViewModelStore()
        val failedStore = ViewModelStore()
        try {
            val healthy =
                ViewModelProvider
                    .create(healthyStore, CartScreenScopeFactory("healthy"), extras)[CartScreenScopeViewModel::class]
            val healthyScope = container.openScope(ShoppingScopes.screen, "healthy")
            val repository = container.getInstance(CartRepository::class, LocalMemoryCart::class)
            val failedScope = container.openScope(ShoppingScopes.screen, "failed")
            failedScope.saveInstance(TemporaryDependency::class, TemporaryDependency())
            val instances = failedScope.readPrivateField("instances") as Map<*, *>
            val scopes = container.readPrivateField("scopes") as Map<*, *>
            container.register(DateFormatter::class, scope = ShoppingScopes.screen) {
                error("포맷터 생성 실패")
            }
            val provider = ViewModelProvider.create(failedStore, CartScreenScopeFactory("failed"), extras)

            assertThatThrownBy { provider[CartScreenScopeViewModel::class] }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("포맷터 생성 실패")

            assertThat(instances).isEmpty()
            assertThat(failedScope.readPrivateField("container")).isNull()
            assertThat(scopes.values.toList()).containsExactly(healthyScope)
            assertThat(healthyScope.getInstance(DateFormatter::class)).isSameInstanceAs(healthy.dateFormatter)
            assertThat(container.getInstance(CartRepository::class, LocalMemoryCart::class)).isSameInstanceAs(repository)

            container.register(DateFormatter::class, scope = ShoppingScopes.screen) {
                DateFormatter(application)
            }
            val retried = provider[CartScreenScopeViewModel::class]
            val reopened = container.openScope(ShoppingScopes.screen, "failed")

            assertThat(reopened).isNotSameInstanceAs(failedScope)
            assertThat(retried.dateFormatter).isNotSameInstanceAs(healthy.dateFormatter)
            assertThat(scopes).hasSize(2)
        } finally {
            healthyStore.clear()
            failedStore.clear()
        }
        assertThat(container.readPrivateField("scopes") as Map<*, *>).isEmpty()
    }

    private fun Any.readPrivateField(name: String): Any? = javaClass.getDeclaredField(name).apply { isAccessible = true }.get(this)

    class TemporaryDependency
}
