package woowacourse.shopping.ui

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.FakeProductRepository
import woowacourse.shopping.fixture.PRODUCT_A_1000
import woowacourse.shopping.fixture.PRODUCT_B_2000
import woowacourse.shopping.getOrAwaitValue

class MainViewModelTest :
    FunSpec({
        lateinit var viewModel: MainViewModel
        lateinit var productRepository: ProductRepository
        lateinit var cartRepository: CartRepository

        beforeTest {
            ArchTaskExecutor.getInstance().setDelegate(
                object : TaskExecutor() {
                    override fun executeOnDiskIO(runnable: Runnable) = runnable.run()

                    override fun postToMainThread(runnable: Runnable) = runnable.run()

                    override fun isMainThread(): Boolean = true
                },
            )
            productRepository = FakeProductRepository()
            cartRepository = FakeCartRepository()
            viewModel = MainViewModel(productRepository, cartRepository)
        }

        afterTest {
            ArchTaskExecutor.getInstance().setDelegate(null)
        }

        test("상품을 조회한다") {
            // given
            val expected = listOf(PRODUCT_A_1000, PRODUCT_B_2000)

            // when
            viewModel.getAllProducts()

            // then
            val actual = viewModel.products.getOrAwaitValue()
            actual shouldContainExactly expected
        }

        test("장바구니에 상품을 추가한다") {
            // given
            val expected = true

            // when
            viewModel.addCartProduct(PRODUCT_B_2000)

            // then
            val actual = viewModel.onProductAdded.getOrAwaitValue()
            actual shouldBe expected
        }
    })
