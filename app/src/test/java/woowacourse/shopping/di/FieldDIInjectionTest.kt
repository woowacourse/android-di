package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.fixture.FieldInjectViewModel
import woowacourse.shopping.fixture.TestAppContainer

class FieldDIInjectionTest {
    private lateinit var container: TestAppContainer
    private lateinit var factory: TestViewModelFactory

    @Before
    fun setup() {
        container = TestAppContainer()
        factory = TestViewModelFactory(container)
    }

    @Test
    fun `Inject_붙은_필드만_주입됨`() {
        val vm = factory.create(FieldInjectViewModel::class.java)

        // 주입된 필드
        assertThat(vm.injectedRepository).isEqualTo(container.fakeProductRepository)

        // 주입되지 않은 필드
        assertThat(vm.notInjectedRepository).isNull()
    }
}
