package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import woowacourse.bibi.di.androidx.InjectingViewModelFactory
import woowacourse.bibi.di.core.ActivityScope
import woowacourse.bibi.di.core.ContainerBuilder
import woowacourse.bibi.di.core.Local
import woowacourse.bibi.di.core.ViewModelScope
import woowacourse.shopping.common.MainDispatcherRule
import woowacourse.shopping.common.getOrAwaitValue
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fake.FakeProductRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.MainViewModel

class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `getAllProducts 호출 시 products LiveData가 업데이트된다`() {
        // given
        val productRepo = FakeProductRepository(ProductFixture.AllProducts)
        val cartRepo = FakeCartRepository()
        val viewModel = createViewModelWith(productRepo, cartRepo)

        // when
        viewModel.getAllProducts()

        // then
        assertEquals(ProductFixture.AllProducts, viewModel.products.getOrAwaitValue())
    }

    @Test
    fun `addCartProduct 호출 시 장바구니에 추가되고 onProductAdded가 true로 바뀐다`() =
        runTest {
            // given
            val productRepo = FakeProductRepository()
            val cartRepo = FakeCartRepository()
            val viewModel = createViewModelWith(productRepo, cartRepo)
            val item = Product(0L, "웨이퍼 초코바", 1000, "")

            // when
            viewModel.addCartProduct(item)

            // then
            assertTrue(viewModel.onProductAdded.getOrAwaitValue())
            assertEquals(listOf(item), cartRepo.getAllCartProducts())
        }

    private class TestOwner : ViewModelStoreOwner {
        override val viewModelStore: ViewModelStore = ViewModelStore()
    }

    private fun createViewModelWith(
        fakeProductRepo: ProductRepository,
        fakeCartRepo: CartRepository,
    ): MainViewModel {
        val container =
            ContainerBuilder()
                .apply {
                    register(ProductRepository::class, Local::class, ViewModelScope::class) { fakeProductRepo }
                    register(CartRepository::class, Local::class, ActivityScope::class) { fakeCartRepo }
                }.build()
        val activityContainer = container.child(ActivityScope::class)
        val owner = TestOwner()
        val factory = InjectingViewModelFactory(activityContainer, owner)

        return factory.create(MainViewModel::class.java)
    }
}
