package woowacourse.shopping.di.inject

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNotSame
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import woowacourse.shopping.di.annotation.CustomInject
import woowacourse.shopping.di.container.DependencyContainer
import woowacourse.shopping.di.inject.fake.FakeDatabaseCartRepository
import woowacourse.shopping.di.inject.fake.FakeViewModel
import woowacourse.shopping.fake.FakeInMemoryCartRepository
import woowacourse.shopping.fake.TestCartInfo
import woowacourse.shopping.repository.CartRepository
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

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
        val datas = listOf("가", "나", "다", "라")
        DependencyContainer.setInstance(
            CartRepository::class,
            customInjector.inject(FakeInMemoryCartRepository::class),
        )
        DependencyContainer.setInstance(
            TestCartInfo::class,
            customInjector.inject(
                TestCartInfo::class,
            ),
        )
        DependencyContainer.setInstance(
            List::class,
            datas,
        )
        val viewModel = customInjector.inject(FakeViewModel::class)

        // then
        assertNotNull(viewModel)
        assertNotNull(viewModel.cartRepository)
        assertEquals(viewModel.cartRepository.javaClass.kotlin, FakeInMemoryCartRepository::class)
        assertEquals(viewModel.datas, datas)
    }

    @Test
    fun `DependencyContainer에서 instance가 없다면 생성하여 주입한다`() {
        // given && when
        val datas = listOf("가", "나", "다", "라")
        DependencyContainer.setInstance(
            CartRepository::class,
            customInjector.inject(FakeInMemoryCartRepository::class),
        )
        DependencyContainer.setInstance(
            List::class,
            datas,
        )
        val viewModel = customInjector.inject(FakeViewModel::class)

        // then
        assertNotNull(viewModel)
        assertNotNull(viewModel.cartRepository)
        assertEquals(viewModel.cartRepository.javaClass.kotlin, FakeInMemoryCartRepository::class)
    }

    @Test
    fun `CustomInject annotation이 없는 경우 의존성 주입이 일어나지 않는다`() {
        // given
        val datas = listOf("가", "나", "다", "라")
        DependencyContainer.setInstance(
            CartRepository::class,
            customInjector.inject(FakeInMemoryCartRepository::class),
        )
        DependencyContainer.setInstance(
            List::class,
            datas,
        )
        val viewModel = customInjector.inject(FakeViewModel::class)
        val datasField = FakeViewModel::class.declaredMemberProperties.find { it.name == "datas" }
        val itemsField = FakeViewModel::class.declaredMemberProperties.find { it.name == "items" }

        // CustomInject Annotation이 있는지 확인
        datasField?.hasAnnotation<CustomInject>()?.let { assert(it) }
        itemsField?.hasAnnotation<CustomInject>()?.let { assertFalse(it) }

        // then
        // CustomInject 어노테이션이 붙은 필드는 주입되고 어노테이션이 안 붙은 필드는 초기화도 되지 않음
        assertEquals(viewModel.datas, datas)
        assertThrows<UninitializedPropertyAccessException> { viewModel.items }
    }

    @Test
    fun `Qualifier에 따라 원하는 인스턴스가 주입된다`() {
        // given
        val datas = listOf("가", "나", "다", "라")
        DependencyContainer.setInstance(
            CartRepository::class,
            customInjector.inject(FakeInMemoryCartRepository::class),
        )
        DependencyContainer.setInstance(
            CartRepository::class,
            customInjector.inject(FakeDatabaseCartRepository::class),
        )
        DependencyContainer.setInstance(
            List::class,
            datas,
        )
        val viewModel = customInjector.inject(FakeViewModel::class)

        // then
        assertEquals(viewModel.cartRepository::class, FakeInMemoryCartRepository::class)
        assertNotSame(viewModel.cartRepository::class, FakeDatabaseCartRepository::class)
    }
}
