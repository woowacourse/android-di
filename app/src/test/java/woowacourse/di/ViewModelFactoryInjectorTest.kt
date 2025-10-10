package woowacourse.di

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import woowacourse.fake.FakeRepositoryModule
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.ViewModelFactoryInjector
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.ui.cart.vm.CartViewModel

class ViewModelFactoryInjectorTest {
    private lateinit var dependencyInjector: DependencyInjector
    private lateinit var viewModelFactory: ViewModelFactoryInjector

    @Before
    fun setup() {
        val repositoryModule = FakeRepositoryModule()
        dependencyInjector = DependencyInjector(listOf(repositoryModule))
        viewModelFactory = ViewModelFactoryInjector(dependencyInjector)
    }

    @Test
    fun `ViewModel이 DI를 통해 정상적으로 생성되고 필요한 의존성이 주입된다`() {
        // given
        val viewModel = viewModelFactory.create(CartViewModel::class.java)
        val field = viewModel::class.java.getDeclaredField("cartRepository")

        // when
        val injectedRepository = field.get(viewModel)

        // then
        assertNotNull(injectedRepository)
        assertTrue(injectedRepository is CartRepository)
    }

    @Test
    fun `등록되지 않은 의존성을 가진 ViewModel 생성 시 예외가 발생한다`() {
        // given
        val emptyContainer = DependencyInjector(emptyList())
        val factory = ViewModelFactoryInjector(emptyContainer)

        // when
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                factory.create(CartViewModel::class.java)
            }

        // then
        val expected = "CartRepository 타입의 의존성이 등록되어있지 않습니다."
        val actual = exception.message
        assertEquals(expected, actual)
    }
}
