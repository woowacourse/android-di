@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.di

import junit.framework.TestCase.assertEquals
import org.junit.Test
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

class AutoViewModelFactoryTest {
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