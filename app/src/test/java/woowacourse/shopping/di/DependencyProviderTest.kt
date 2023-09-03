package woowacourse.shopping.di

import junit.framework.TestCase.assertEquals
import org.junit.Test
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

class DependencyProviderTest {
    @Test
    fun `createInstance 함수를 통해 MainViewModel을 생성한다`() {
        // given
        val name = MainViewModel::class.simpleName

        // when
        val mainViewModel = DependencyProvider.getInstance().createInstance(MainViewModel::class)

        // then
        assertEquals(mainViewModel::class.simpleName, name)
    }

    @Test
    fun `createInstance 함수를 통해 CartViewModel을 생성한다`() {
        // given
        val name = CartViewModel::class.simpleName

        // when
        val cartViewModel = DependencyProvider.getInstance().createInstance(CartViewModel::class)

        // then
        assertEquals(cartViewModel::class.simpleName, name)
    }

    @Test
    fun `createInstance 함수를 통해 FakeViewModel을 생성한다`() {
        // given
        val name = FakeViewModel::class.simpleName

        // when
        val fakeViewModel = DependencyProvider.getInstance().createInstance(FakeViewModel::class)

        // then
        assertEquals(fakeViewModel::class.simpleName, name)
    }
}

class FakeViewModel()
