package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.FakeCartRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.CartedProduct

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    private lateinit var viewModel: CartViewModel
    private lateinit var cartRepository: CartRepository
    private val testDispatcher = StandardTestDispatcher()
    private var currentTime: Long? = null

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() =
        runTest {
            currentTime = System.currentTimeMillis()
            Dispatchers.setMain(testDispatcher)
            cartRepository = FakeCartRepository(currentTime ?: return@runTest)
            viewModel = CartViewModel()

            addCartProducts(3)
            val clazz = viewModel::class
            val cartRepositoryProp = clazz.java.getDeclaredField("cartRepository")
            cartRepositoryProp.isAccessible = true
            cartRepositoryProp.set(viewModel, cartRepository)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `카트에 담긴 모든 상품을 가져올 수 있다`() =
        runTest {
            // when
            viewModel.getAllCartProducts()
            advanceUntilIdle()

            // then
            val currentTimeInMillis = currentTime ?: return@runTest
            val cartProducts = viewModel.cartProducts.getOrAwaitValue()
            assertThat(cartProducts).isEqualTo(
                listOf(
                    CartedProduct(1, "Product1", 1000, "image1", createdAt = currentTimeInMillis),
                    CartedProduct(2, "Product2", 2000, "image2", createdAt = currentTimeInMillis),
                    CartedProduct(3, "Product3", 3000, "image3", createdAt = currentTimeInMillis),
                ),
            )
        }

    @Test
    fun `카트에 담긴 상품을 삭제할 수 있다`() =
        runTest {
            // when
            viewModel.getAllCartProducts()
            advanceUntilIdle()
            viewModel.deleteCartProduct(1)
            advanceUntilIdle()
            viewModel.getAllCartProducts()
            advanceUntilIdle()

            // then
            val currentTimeInMillis = currentTime ?: return@runTest
            val cartProducts = viewModel.cartProducts.getOrAwaitValue()
            val onCartProductDeleted = viewModel.onCartProductDeleted.getOrAwaitValue()

            assertThat(cartProducts).isEqualTo(
                listOf(
                    CartedProduct(2, "Product2", 2000, "image2", currentTimeInMillis),
                    CartedProduct(3, "Product3", 3000, "image3", currentTimeInMillis),
                ),
            )

            assertThat(onCartProductDeleted).isTrue()
        }

    private suspend fun addCartProducts(size: Int) {
        repeat(size) { index ->
            addCartProduct(index + 1)
        }
    }

    private suspend fun addCartProduct(index: Int) {
        cartRepository.addCartProduct(generateSingleProduct(index))
    }
}
