package woowacourse.shopping.di

import junit.framework.TestCase.assertNotNull
import org.junit.Test
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

class DependencyProviderTest {
    @Test
    fun `createInstance 함수를 통해 MainViewModel을 생성한다`() {
        // when
        val mainViewModel = DependencyProvider.getInstance().createInstance(MainViewModel::class)

        // then
        assertNotNull(mainViewModel)
    }

    @Test
    fun `createInstance 함수를 통해 CartViewModel을 생성한다`() {
        // when
        val cartViewModel = DependencyProvider.getInstance().createInstance(CartViewModel::class)

        // then
        assertNotNull(cartViewModel)
    }

    @Test
    fun `createInstance 함수를 통해 FakeViewModel을 생성한다`() {
        // when
        val fakeViewModel = DependencyProvider.getInstance().createInstance(FakeViewModel::class)

        // then
        assertNotNull(fakeViewModel)
    }
}

class FakeViewModel()
