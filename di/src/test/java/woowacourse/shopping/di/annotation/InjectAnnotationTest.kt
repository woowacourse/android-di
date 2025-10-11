package woowacourse.shopping.di.annotation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.di.DependencyContainer
import woowacourse.shopping.di.ViewModelFactoryInjector
import woowacourse.shopping.di.fake.FakeDependencyModule
import woowacourse.shopping.di.fake.FakeViewModel
import woowacourse.shopping.di.fake.repository.cart.CartRepository

class InjectAnnotationTest {
    private lateinit var fakeDependencyModule: FakeDependencyModule
    private lateinit var diContainer: DependencyContainer
    private lateinit var vm: FakeViewModel

    @Before
    fun setup() {
        fakeDependencyModule = FakeDependencyModule()
        diContainer = DependencyContainer(listOf(fakeDependencyModule))
        vm = ViewModelFactoryInjector(diContainer).create(FakeViewModel::class.java)
    }

    @Test
    fun `@Inject가 붙은 필드, 생성자 주입이 모두 성공하고, @Inject를 붙이지 않으면 주입되지 않는다`() {
        // given
        diContainer.inject(vm)

        val injectedStringField =
            vm::class.java.getDeclaredField("injectedString")
                .apply { isAccessible = true }
        val nonInjectedStringField =
            vm::class.java.getDeclaredField("nonInjectedString")
                .apply { isAccessible = true }

        val myCartRepositoryField =
            vm::class.java.getDeclaredField("myCartRepository")

        // when
        val injectedValue = injectedStringField.get(vm)
        val nonInjectedValue = nonInjectedStringField.get(vm)
        val myCartRepository = myCartRepositoryField.get(vm)

        // then
        assertEquals("inject", injectedValue)
        assertEquals(null, nonInjectedValue)
        assertTrue(myCartRepository is CartRepository)
    }

    @Test
    fun `@Inject가 없는 lateinit var 접근 시 UninitializedPropertyAccessException 에러가 발생한다`() {
        assertThrows(UninitializedPropertyAccessException::class.java) {
            vm.nonInjectedString
        }
    }
}
