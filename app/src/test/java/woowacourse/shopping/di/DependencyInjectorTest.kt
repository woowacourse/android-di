package woowacourse.shopping.di

import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.di.DependencyInjector.inject
import woowacourse.shopping.ui.MainViewModel

class DependencyInjectorTest {

    @Test(expected = IllegalStateException::class)
    fun `Singleton에 CartRepository만 존재하면 ViewModel 생성에 실패한다`() {
        // given
        DependencyInjector.dependencies = object : Dependencies {
            val cartRepository: CartRepository by lazy { DefaultCartRepository() }
        }

        // when
        inject<MainViewModel>()

        // then
    }
}
