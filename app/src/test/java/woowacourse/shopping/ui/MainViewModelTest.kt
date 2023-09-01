package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.DependencyInjector.inject
import woowacourse.shopping.di.Singleton
import woowacourse.shopping.model.Product

@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `장바구니에 상품을 등록하면 상품 등록 상태가 true가 되고 CartRepository에 Product가 추가된다`() {
        // given
        val fakeCartRepository = object : CartRepository {
            val products: MutableList<Product> = mutableListOf()
            override fun addCartProduct(product: Product) {
                products.add(product)
            }

            override fun getAllCartProducts(): List<Product> {
                TODO("Not yet implemented")
            }

            override fun deleteCartProduct(id: Int) {
                TODO("Not yet implemented")
            }
        }

        DependencyInjector.singleton = object : Singleton {
            val cartRepository: CartRepository by lazy { fakeCartRepository }
            val productRepository: ProductRepository by lazy { DefaultProductRepository() }
        }

        val viewModel = inject<MainViewModel>()

        // when
        val expect = Product("", 0, "")
        viewModel.addCartProduct(expect)

        // then
        assertEquals(true, viewModel.onProductAdded.value)
        assertEquals(expect, fakeCartRepository.products[0])
    }
}
