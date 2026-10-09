package woowacourse.shopping.ui.viewmodel

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.cksckckcks.di.DiContainer
import com.cksckckcks.di.ScopedContainer
import com.google.common.truth.Truth.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.LocalMemoryCart
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class ViewModelScopeTest {
    private val application get() = RuntimeEnvironment.getApplication() as ShoppingApplication
    private val container get() = application.container.diContainer

    @Test
    fun `같은 ViewModel을 다시 조회하면 저장소와 스코프를 유지한다`() {
        val store = ViewModelStore()
        try {
            val provider = provider(store)
            val first = provider[ProductsViewModel::class]
            val second = provider[ProductsViewModel::class]
            val scope = container.scopes().values.single() as ScopedContainer

            assertThat(second).isSameInstanceAs(first)
            assertThat(second.readPrivateField("productRepository")).isSameInstanceAs(first.readPrivateField("productRepository"))
            assertThat(scope.getInstance(ProductRepository::class)).isSameInstanceAs(first.readPrivateField("productRepository"))
            first.getAllProducts()
            assertThat(first.uiState.value.products).hasSize(3)
        } finally {
            store.clear()
        }
    }

    @Test
    fun `같은 클래스의 서로 다른 ViewModel은 상품 저장소만 분리한다`() {
        val store = ViewModelStore()
        try {
            val provider = provider(store)
            val first = provider.get("first", ProductsViewModel::class)
            val second = provider.get("second", ProductsViewModel::class)

            assertThat(first.readPrivateField("productRepository")).isNotSameInstanceAs(second.readPrivateField("productRepository"))
            assertThat(first.readPrivateField("cartRepository")).isSameInstanceAs(second.readPrivateField("cartRepository"))
            assertThat(container.scopes()).hasSize(2)
        } finally {
            store.clear()
        }
    }

    @Test
    fun `ViewModelStore를 비우면 해당 스코프의 참조만 제거한다`() {
        val firstStore = ViewModelStore()
        val secondStore = ViewModelStore()
        try {
            val first = provider(firstStore)[ProductsViewModel::class]
            val firstScope = container.scopes().values.single() as ScopedContainer
            val instances = firstScope.readPrivateField("instances") as Map<*, *>
            val second = provider(secondStore)[ProductsViewModel::class]
            val cartRepository = first.readPrivateField("cartRepository")

            firstStore.clear()

            assertThat(instances).isEmpty()
            assertThat(firstScope.readPrivateField("container")).isNull()
            assertThat(container.scopes()).hasSize(1)
            assertThat(second.readPrivateField("cartRepository")).isSameInstanceAs(cartRepository)
            assertThat(container.getInstance(CartRepository::class, LocalMemoryCart::class)).isSameInstanceAs(cartRepository)
            assertThatThrownBy { firstScope.getInstance(ProductRepository::class) }.isInstanceOf(IllegalStateException::class.java)
            second.getAllProducts()
            assertThat(second.uiState.value.products).hasSize(3)
        } finally {
            firstStore.clear()
            secondStore.clear()
        }
        assertThat(container.scopes()).isEmpty()
    }

    @Test
    fun `구성 변경에는 같은 ViewModel과 저장소를 유지하고 Activity 종료 때 정리한다`() {
        val controller = Robolectric.buildActivity(ComponentActivity::class.java).setup()
        try {
            val firstActivity = controller.get()
            val first = provider(firstActivity.viewModelStore)[ProductsViewModel::class]
            val repository = first.readPrivateField("productRepository")

            controller.recreate()

            val secondActivity = controller.get()
            val second = provider(secondActivity.viewModelStore)[ProductsViewModel::class]
            assertThat(secondActivity).isNotSameInstanceAs(firstActivity)
            assertThat(second).isSameInstanceAs(first)
            assertThat(second.readPrivateField("productRepository")).isSameInstanceAs(repository)
            assertThat(container.scopes()).hasSize(1)
        } finally {
            controller.pause().stop().destroy()
        }
        assertThat(container.scopes()).isEmpty()
    }

    @Test
    fun `ViewModel 생성 실패 시 열린 스코프를 정리한다`() {
        val store = ViewModelStore()
        assertThatThrownBy { provider(store)[FailingViewModel::class] }.hasRootCauseMessage("생성 실패")
        assertThat(container.scopes()).isEmpty()
        store.clear()
    }

    @Test
    fun `ViewModel 생성과 종료를 반복해도 저장소와 스코프가 누적되지 않는다`() {
        var previousRepository: Any? = null
        repeat(5) {
            val store = ViewModelStore()
            try {
                val viewModel = provider(store)[ProductsViewModel::class]
                val repository = viewModel.readPrivateField("productRepository")
                assertThat(repository).isNotSameInstanceAs(previousRepository)
                assertThat(container.scopes()).hasSize(1)
                previousRepository = repository
            } finally {
                store.clear()
            }
            assertThat(container.scopes()).isEmpty()
        }
    }

    private fun provider(store: ViewModelStore): ViewModelProvider {
        val extras = MutableCreationExtras().apply { this[APPLICATION_KEY] = application }
        return ViewModelProvider.create(store, ViewModelFactory, extras)
    }

    private fun DiContainer.scopes(): Map<*, *> = readPrivateField("scopes") as Map<*, *>

    private fun Any.readPrivateField(name: String): Any? = javaClass.getDeclaredField(name).apply { isAccessible = true }.get(this)

    class FailingViewModel(
        val repository: ProductRepository,
    ) : ViewModel() {
        init {
            error("생성 실패")
        }
    }
}
