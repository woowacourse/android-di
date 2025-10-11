package woowacourse.shopping.di

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.di.fake.FakeDependencyModule
import woowacourse.shopping.di.fake.FakeViewModel
import woowacourse.shopping.di.fake.repository.cart.CartRepository

class ViewModelFactoryInjectorTest {
    private lateinit var dependencyContainer: DependencyContainer
    private lateinit var viewModelFactory: ViewModelFactoryInjector

    @Before
    fun setup() {
        dependencyContainer = DependencyContainer(listOf(FakeDependencyModule()))
        viewModelFactory = ViewModelFactoryInjector(dependencyContainer)
    }

    @Test
    fun `ViewModel이 DI를 통해 정상적으로 생성되고 필요한 의존성이 주입된다`() {
        // given
        val viewModel = viewModelFactory.create(FakeViewModel::class.java)
        val field = viewModel::class.java.getDeclaredField("myCartRepository")

        // when
        val injectedRepository = field.get(viewModel)

        // then
        assertTrue(injectedRepository is CartRepository)
    }

    @Test
    fun `등록되지 않은 의존성을 가진 ViewModel 생성 시 예외가 발생한다`() {
        // given
        val emptyContainer = DependencyContainer(emptyList())
        val factory = ViewModelFactoryInjector(emptyContainer)

        // when
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                factory.create(FakeViewModel::class.java)
            }

        // then
        val expected = "String 타입의 의존성이 등록되어있지 않습니다."
        val actual = exception.message
        assertEquals(expected, actual)
    }
}
