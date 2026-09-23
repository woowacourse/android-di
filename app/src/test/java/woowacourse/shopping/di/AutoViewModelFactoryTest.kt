@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.di

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.FakeCartProductDao
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

class AutoViewModelFactoryTest {
    @Before
    fun setUp() {
        DependencyContainer.register(
            CartProductDao::class,
            FakeCartProductDao(),
        )
    }

    @Test
    fun `CartViewModel을 요청하면 CartViewModel이 생성된다`() {
        val viewModel = AutoViewModelFactory().create(CartViewModel::class.java)

        assertEquals(viewModel::class.java, CartViewModel::class.java)
    }

    @Test
    fun `ProductsViewModel을 요청하면 ProductsViewModel이 생성된다`() {
        val viewModel = AutoViewModelFactory().create(ProductsViewModel::class.java)

        assertEquals(viewModel::class.java, ProductsViewModel::class.java)
    }
}
