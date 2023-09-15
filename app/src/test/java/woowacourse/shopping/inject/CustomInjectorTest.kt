package woowacourse.shopping.inject

import com.lope.di.annotation.CustomInject
import com.lope.di.container.DependencyContainer
import com.lope.di.inject.CustomInjector
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNotSame
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.fake.FakeInMemoryCartRepository
import woowacourse.shopping.fake.TestCartInfo
import woowacourse.shopping.inject.fake.FakeDatabaseCartRepository
import woowacourse.shopping.inject.fake.FakeViewModel
import woowacourse.shopping.repository.CartRepository
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

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
    fun `TestCartInfo instance를 주입해주지 않는다면 DependencyContainer에서 생성하여 주입한다`() {
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
        // TestCartInfo instance를 주입해주지 않음을 확인
        assertNull(DependencyContainer.getInstance(TestCartInfo::class, null))
        val viewModel = customInjector.inject(FakeViewModel::class)

        // then
        assertNotNull(viewModel)
        assertNotNull(viewModel.cartRepository)
        // viewModel 생성자 파라미터안에 있는 타입이 TestCartInfo임을 확인
        assertEquals(viewModel.cartRepository::class.primaryConstructor?.parameters?.first()?.type?.jvmErasure, TestCartInfo::class)
        assertNotNull(viewModel.cartRepository::class.primaryConstructor?.parameters)
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
        assertThrows(UninitializedPropertyAccessException::class.java) {
            viewModel.items
        }
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
