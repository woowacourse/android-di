package woowacourse.shopping.ui

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.PRODUCT_A_1000
import woowacourse.shopping.fixture.PRODUCT_B_2000
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.ui.cart.CartViewModel

class CartViewModelTest :
    StringSpec({
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
            repository =
                FakeCartRepository().apply {
                    addCartProduct(PRODUCT_A_1000)
                    addCartProduct(PRODUCT_B_2000)
                }
            viewModel = CartViewModel(repository)
        }

        afterTest {
            ArchTaskExecutor.getInstance().setDelegate(null)
        }

        "장바구니 모든 데이터를 가져온다" {
            // given
            val expected = listOf(PRODUCT_A_1000, PRODUCT_B_2000)

            // when
            viewModel.getAllCartProducts()

            // then
            val actual = viewModel.cartProducts.getOrAwaitValue()
            actual shouldContainExactly expected
        }

        "장바구니에서 상품 하나가 제거된다" {
            // given
            val expected = true

            // when
            viewModel.deleteCartProduct(0)

            // then
            val actual = viewModel.onCartProductDeleted.getOrAwaitValue()
            actual shouldBe expected
        }

        "장바구니에서 상품 하나 제거된 후 남은 상품들을 가져온다" {
            // given
            val expected = listOf(PRODUCT_B_2000)

            // when
            viewModel.deleteCartProduct(0)
            viewModel.getAllCartProducts()

            // then
            val actual = viewModel.cartProducts.getOrAwaitValue()
            actual shouldBe expected
        }
    })
