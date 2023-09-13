package woowacourse.shopping.data

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.CartProduct
import woowacourse.shopping.FakeCartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.DatabaseIdentifier

@OptIn(ExperimentalCoroutinesApi::class)
class CartRepositoryTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun 장바구니의_모든_상품을_가져온다() = runTest {
        // given
        val products = listOf(CartProduct())
        val cartRepository = FakeCartRepository(products)

        // when
        val actual = cartRepository.getAllCartProducts()

        // then
        assertEquals(products, actual)
    }

    @Test
    fun 장바구니의_0번째_상품을_삭제한다() = runTest {

        // given
        val products = listOf(
            CartProduct(identifier = DatabaseIdentifier(0)),
            CartProduct(identifier = DatabaseIdentifier(1))
        )
        val cartRepository = FakeCartRepository(products)

        // when
        cartRepository.deleteCartProduct(DatabaseIdentifier(0))

        // then
        val expect = listOf(CartProduct(identifier = DatabaseIdentifier(1)))
        val actual = cartRepository.products

        assertEquals(expect, actual)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun 장바구니에_존재하지_않는_상품을_삭제하면_IndexOutOfBoundsException_예외가_발생한다() = runTest {
        // given
        val products = emptyList<CartProduct>()
        val cartRepository = FakeCartRepository(products)

        // when
        cartRepository.deleteCartProduct(DatabaseIdentifier(0))

        // then
    }
}
