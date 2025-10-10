package woowacourse.shopping.ui

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.PRODUCT_1L_A_1000
import woowacourse.shopping.fixture.PRODUCT_2L_B_2000
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.ui.cart.CartViewModel

class CartViewModelTest :
    StringSpec({
        val testDispatcher = StandardTestDispatcher()

        lateinit var viewModel: CartViewModel
        lateinit var repository: CartRepository

        beforeTest {
            ArchTaskExecutor.getInstance().setDelegate(
                object : TaskExecutor() {
                    override fun executeOnDiskIO(runnable: Runnable) = runnable.run()

                    override fun postToMainThread(runnable: Runnable) = runnable.run()

                    override fun isMainThread(): Boolean = true
                },
            )

            Dispatchers.setMain(testDispatcher)

            repository =
                FakeCartRepository().apply {
                    addCartProduct(PRODUCT_1L_A_1000)
                    addCartProduct(PRODUCT_2L_B_2000)
                }
            viewModel = CartViewModel(repository)
        }

        afterTest {
            ArchTaskExecutor.getInstance().setDelegate(null)
            Dispatchers.resetMain()
        }

        "장바구니 모든 데이터를 가져온다" {
            // given
            val expected = listOf(PRODUCT_1L_A_1000, PRODUCT_2L_B_2000)

            // when
            viewModel.getAllCartProducts()
            testDispatcher.scheduler.advanceUntilIdle()

            // then
            val actual = viewModel.cartProducts.getOrAwaitValue()
            actual.size shouldBeEqual expected.size
            actual[0].name shouldBeEqual expected[0].name
            actual[1].name shouldBeEqual expected[1].name
        }

        "장바구니에서 상품 하나가 제거된다" {
            // given
            val expected = true

            // when
            viewModel.deleteCartProduct(0)
            testDispatcher.scheduler.advanceUntilIdle()

            // then
            val actual = viewModel.onCartProductDeleted.getOrAwaitValue()
            actual shouldBe expected
        }

        "장바구니에서 상품 하나 제거된 후 남은 상품들을 가져온다" {
            // given
            val expected = PRODUCT_2L_B_2000

            // when
            viewModel.deleteCartProduct(1L)
            viewModel.getAllCartProducts()
            testDispatcher.scheduler.advanceUntilIdle()

            // then
            val actual = viewModel.cartProducts.getOrAwaitValue()

            actual.size shouldBe 1
            actual[0].run {
                name shouldBe expected.name
                price shouldBe expected.price
                imageUrl shouldBe expected.imageUrl
            }
        }
    })
