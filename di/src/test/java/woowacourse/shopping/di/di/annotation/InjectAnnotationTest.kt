package woowacourse.shopping.di.di.annotation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.fake.FakeDependencyModule
import woowacourse.shopping.di.fake.FakeViewModel

class InjectAnnotationTest {
    private lateinit var dependencyModule: FakeDependencyModule
    private lateinit var diContainer: DependencyInjector
    private lateinit var vm: FakeViewModel

    @Before
    fun setup() {
        vm = FakeViewModel()
        dependencyModule = FakeDependencyModule()
        diContainer = DependencyInjector(listOf(dependencyModule))
    }

    @Test
    fun `@Inject가 붙은 프로퍼티를 주입한다`() {
        diContainer.inject(vm)

        assertEquals("inject", vm.injectedString)
    }

    @Test
    fun `@Inject가 없는 lateinit var 접근 시 UninitializedPropertyAccessException 에러가 발생한다`() {
        assertThrows(UninitializedPropertyAccessException::class.java) {
            vm.nonInjectedString
        }
    }
}
