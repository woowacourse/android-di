package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.fixture.FieldInjectViewModel
import woowacourse.shopping.fixture.TestAppContainer

class FieldDIInjectionTest {
    private lateinit var container: TestAppContainer
    private lateinit var factory: FieldInjectViewModelFactory

    @Test
    fun `Inject_붙은_필드만_주입됨`() {
        container = TestAppContainer()
        factory = FieldInjectViewModelFactory(container)
        val vm = factory.create(FieldInjectViewModel::class.java)

        // 주입된 필드
        assertThat(vm.injectedRepository).isEqualTo(container.fakeProductRepository)

        // 주입되지 않은 필드
        assertThat(vm.notInjectedRepository).isNull()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Inject_붙은_필드_바인딩_없으면_예외`() {
        container = TestAppContainer()
        factory = FieldInjectViewModelFactory(container)

        factory.create(FieldInjectViewModel::class.java)
    }
}
