package woowacourse.shopping.di.inject

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.di.container.DependencyContainer
import woowacourse.shopping.di.inject.fake.FakeViewModel
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fake.TestCartInfo
import woowacourse.shopping.repository.CartRepository

class CustomInjectorTest {

    private lateinit var customInjector: CustomInjector

    @Before
    fun setUp() {
        customInjector = CustomInjector()
    }

    @After
    fun tearDown() {
        DependencyContainer.clear()
    }

    @Test
    fun `DependencyContainer에서 instance를 찾아 주입한다 `() {
        // given && when
        DependencyContainer.setInstance(
            CartRepository::class,
            customInjector.inject(FakeCartRepository::class),
        )
        DependencyContainer.setInstance(
            TestCartInfo::class,
            customInjector.inject(
                TestCartInfo::class,
            ),
        )
        val viewModel = customInjector.inject(FakeViewModel::class)

        // then
        assertNotNull(viewModel)
        assertNotNull(viewModel.cartRepository)
        assertEquals(viewModel.cartRepository.javaClass.kotlin, FakeCartRepository::class)
    }

    @Test
    fun `DependencyContainer에서 instance가 없다면 생성하여 주입한다`() {
        // given
        DependencyContainer.setInstance(
            CartRepository::class,
            customInjector.inject(FakeCartRepository::class),
        )
        val viewModel = customInjector.inject(FakeViewModel::class)

        // then
        assertNotNull(viewModel)
        assertNotNull(viewModel.cartRepository)
        assertEquals(viewModel.cartRepository.javaClass.kotlin, FakeCartRepository::class)
    }
}
