package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.fixture.ConstructorTestViewModel
import woowacourse.shopping.fixture.ConstructorTestViewModelWithDefaultDependency
import woowacourse.shopping.fixture.ConstructorTestViewModelWithDependency
import woowacourse.shopping.fixture.TestAppContainer
import woowacourse.shopping.fixture.ViewModelWithUnregisteredDependency

class ConstructorDIInjectionTest {
    private lateinit var container: TestAppContainer
    private lateinit var factory: ConstructorInjectViewModelFactory

    @Before
    fun setup() {
        container = TestAppContainer()
        factory = ConstructorInjectViewModelFactory(container)
    }

    @Test
    fun `ViewModel_의존성이_없으면_정상_생성`() {
        val vm = factory.create(ConstructorTestViewModel::class.java)
        assertThat(vm).isNotNull()
    }

    @Test
    fun `AppContainer에_선언된_의존성은_주입됨`() {
        val vm = factory.create(ConstructorTestViewModelWithDependency::class.java)
        assertThat(vm.repository).isEqualTo(container.fakeProductRepository)
    }

    @Test
    fun `AppContainer에_선언되고_default_parameter가_있으면_Container_것_주입됨`() {
        val vm = factory.create(ConstructorTestViewModelWithDefaultDependency::class.java)
        // Default 값 무시하고 Container의 인스턴스 사용
        assertThat(vm.repository).isEqualTo(container.fakeProductRepository)
    }

    @Test
    fun `AppContainer에_없는_의존성은_생성_실패하고_에러_메세지가_표시된다`() {
        try {
            factory.create(ViewModelWithUnregisteredDependency::class.java)
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("AppContainer에 repository(woowacourse.shopping.fixture.UnregisteredRepository) 타입의 의존성이 없습니다.")
        }
    }
}
