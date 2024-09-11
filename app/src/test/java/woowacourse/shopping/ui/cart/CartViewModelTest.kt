package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.FakeCartRepository
import woowacourse.shopping.MainDispatcherRule
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.fakeCartProducts
import woowacourse.shopping.fakeDependencyContainer
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.fakeProducts
import woowacourse.shopping.ui.util.DependencyInjector

class CartViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var cartViewModel: CartViewModel

    @Before
    fun setUp() {
        DependencyInjector.initDependencyContainer(fakeDependencyContainer)
        cartViewModel = DependencyInjector.createInstanceFromConstructor(CartViewModel::class.java)
    }

    @Test
    fun `장바구니에_담긴_모든_물품들을_조회한다`() = runTest {
        cartViewModel.getAllCartProducts()
        val value = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(value[2].name).isEqualTo("우테코 삼겹살")
    }

    @Test
    fun `장바구니에_담긴_물품을_제거한다`() = runTest {
        val firstProduct = fakeProducts.first()
        cartViewModel.deleteCartProduct(0)
        val value = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(value).doesNotContain(firstProduct)
    }
}
