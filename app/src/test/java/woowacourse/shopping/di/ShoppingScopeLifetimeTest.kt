package woowacourse.shopping.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.di.DiContainer
import woowacourse.di.ScopeContext
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.cart.CartScreenScopeViewModel
import woowacourse.shopping.ui.cart.DateFormatter
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
class ShoppingScopeLifetimeTest {
    @Test
    fun `백스택 엔트리의 ViewModelStore가 정리되면 화면 의존성 참조를 제거한다`() {
        RuntimeEnvironment.getApplication()
        val scopeId = "screen-test:${UUID.randomUUID()}"
        val firstStore = ViewModelStore()
        val firstProvider = ViewModelProvider.create(firstStore, AoDi.factoryFor(scopeId))
        val firstScope = firstProvider[CartScreenScopeViewModel::class]

        assertThat(firstProvider[CartScreenScopeViewModel::class]).isSameAs(firstScope)

        firstStore.clear()

        val reopenedStore = ViewModelStore()
        val reopenedScope =
            ViewModelProvider
                .create(reopenedStore, AoDi.factoryFor(scopeId))[CartScreenScopeViewModel::class]

        assertThat(reopenedScope.dateFormatter).isNotSameAs(firstScope.dateFormatter)
        reopenedStore.clear()
    }

    @Test
    fun `같은 스코프에서는 수명별 의존성을 재사용한다`() {
        val graph = ShoppingScopeGraph()
        val context = graph.open("vm-A", "screen-A")

        val firstCartRepository = graph.cartRepository(context)
        val firstProductRepository = graph.productRepository(context)
        val firstDateFormatter = graph.dateFormatter(context)

        assertThat(graph.cartRepository(context)).isSameAs(firstCartRepository)
        assertThat(graph.productRepository(context)).isSameAs(firstProductRepository)
        assertThat(graph.dateFormatter(context)).isSameAs(firstDateFormatter)
    }

    @Test
    fun `다른 스코프에서는 앱 의존성만 공유하고 나머지는 분리한다`() {
        val graph = ShoppingScopeGraph()
        val firstContext = graph.open("vm-A", "screen-A")
        val secondContext = graph.open("vm-B", "screen-B")

        assertThat(graph.cartRepository(secondContext)).isSameAs(graph.cartRepository(firstContext))
        assertThat(graph.productRepository(secondContext)).isNotSameAs(graph.productRepository(firstContext))
        assertThat(graph.dateFormatter(secondContext)).isNotSameAs(graph.dateFormatter(firstContext))
    }

    @Test
    fun `스코프를 종료하면 참조를 제거하고 다시 열 때 새 의존성을 만든다`() {
        val graph = ShoppingScopeGraph()
        val context = graph.open("vm-A", "screen-A")
        val cartRepository = graph.cartRepository(context)
        val productRepository = graph.productRepository(context)
        val dateFormatter = graph.dateFormatter(context)

        graph.close("vm-A", "screen-A")

        assertThatThrownBy { graph.productRepository(context) }
            .hasMessageContaining("열리지 않은 스코프입니다: vm-A")
        assertThatThrownBy { graph.dateFormatter(context) }
            .hasMessageContaining("열리지 않은 스코프입니다: screen-A")

        val reopenedContext = graph.open("vm-A", "screen-A")

        assertThat(graph.cartRepository(reopenedContext)).isSameAs(cartRepository)
        assertThat(graph.productRepository(reopenedContext)).isNotSameAs(productRepository)
        assertThat(graph.dateFormatter(reopenedContext)).isNotSameAs(dateFormatter)
    }

    private class ShoppingScopeGraph {
        private val container = DiContainer()
        private val cartRepository = FakeCartRepository()

        init {
            container.openScope(ShoppingScopes.APPLICATION_SCOPE_ID)
            container.registerScopeRule(Context::class, ShoppingScopes.application)
            container.registerScopeRule(CartRepository::class, ShoppingScopes.application)
            container.registerScopeRule(ProductRepository::class, ShoppingScopes.viewModel)
            container.registerScopeRule(DateFormatter::class, ShoppingScopes.screen)
            container.register(
                Context::class,
                RuntimeEnvironment.getApplication(),
                scopeContext = ShoppingScopes.applicationContext,
            )
            container.register(
                CartRepository::class,
                cartRepository,
                RoomCart::class,
                ShoppingScopes.applicationContext,
            )
        }

        fun open(
            viewModelScopeId: String,
            screenScopeId: String,
        ): ScopeContext {
            container.openScope(viewModelScopeId)
            container.openScope(screenScopeId)
            return scopeContext(viewModelScopeId, screenScopeId)
        }

        fun close(
            viewModelScopeId: String,
            screenScopeId: String,
        ) {
            container.closeScope(viewModelScopeId)
            container.closeScope(screenScopeId)
        }

        fun cartRepository(context: ScopeContext): CartRepository = container.instantiate(CartRepository::class, RoomCart::class, context)

        fun productRepository(context: ScopeContext): ProductRepository =
            container.instantiate(ProductRepository::class, scopeContext = context)

        fun dateFormatter(context: ScopeContext): DateFormatter = container.instantiate(DateFormatter::class, scopeContext = context)

        private fun scopeContext(
            viewModelScopeId: String,
            screenScopeId: String,
        ): ScopeContext =
            ScopeContext(
                scopeIds =
                    mapOf(
                        ShoppingScopes.application to ShoppingScopes.APPLICATION_SCOPE_ID,
                        ShoppingScopes.viewModel to viewModelScopeId,
                        ShoppingScopes.screen to screenScopeId,
                    ),
            )
    }

    private class FakeCartRepository : CartRepository {
        override suspend fun addCartProduct(product: Product) = Unit

        override suspend fun getAllCartProducts(): List<CartProduct> = emptyList()

        override suspend fun deleteCartProduct(id: Long) = Unit
    }
}
