package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.fixture.ConstructorTestViewModel
import woowacourse.shopping.fixture.ConstructorTestViewModelWithDefaultDependency
import woowacourse.shopping.fixture.ConstructorTestViewModelWithDependency
import woowacourse.shopping.fixture.TestAppContainer
import woowacourse.shopping.fixture.ViewModelWithUnregisteredDependency

class DIInjectionTest {
    private lateinit var container: TestAppContainer
    private lateinit var factory: TestViewModelFactory

    @Before
    fun setup() {
        container = TestAppContainer()
        factory = TestViewModelFactory(container)
    }

    @Test
    fun `ViewModel 의존성이 없으면 정상 생성`() {
        val vm = factory.create(ConstructorTestViewModel::class.java)
        assertThat(vm).isNotNull()
    }

    @Test
    fun `AppContainer에 선언된 의존성은 주입됨`() {
        val vm = factory.create(ConstructorTestViewModelWithDependency::class.java)
        assertThat(vm.repository).isEqualTo(container.fakeProductRepository)
    }

    @Test
    fun `AppContainer에 선언되고 default parameter가 있으면 Container 것 주입됨`() {
        val vm = factory.create(ConstructorTestViewModelWithDefaultDependency::class.java)
        // Default 값 무시하고 Container의 인스턴스 사용
        assertThat(vm.repository).isEqualTo(container.fakeProductRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `AppContainer에 없는 의존성은 생성 실패`() {
        factory.create(ViewModelWithUnregisteredDependency::class.java)
    }
}
