package woowacourse.shopping.ui.cart

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
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.di.AoDi
import woowacourse.shopping.di.RoomCart
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `상품을 삭제한 다음 목록을 다시 읽는다`() =
        runTest {
            val product = CartProduct(id = 42L, name = "우테코 과자", price = 10_000, imageUrl = "", createdAt = 0L)
            val repository = RecordingCartRepository(mutableListOf(product))
            AoDi.register(CartRepository::class, repository, RoomCart::class)
            val viewModel = CartViewModel().also(AoDi::inject)

            viewModel.deleteCartProduct(product.id)
            advanceUntilIdle()

            assertThat(repository.operations).containsExactly("delete:42", "getAll").inOrder()
            assertThat(viewModel.uiState.value.cartProducts).isEmpty()
        }

    private class RecordingCartRepository(
        private val products: MutableList<CartProduct>,
    ) : CartRepository {
        val operations = mutableListOf<String>()

        override suspend fun addCartProduct(product: Product) {
            error("이 테스트에서는 상품을 추가하지 않습니다")
        }

        override suspend fun getAllCartProducts(): List<CartProduct> {
            operations += "getAll"
            return products.toList()
        }

        override suspend fun deleteCartProduct(id: Long) {
            operations += "delete:$id"
            products.removeAll { it.id == id }
        }
    }
}
