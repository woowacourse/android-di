import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fake.FakeDefaultCartRepository
import woowacourse.shopping.fake.FakeModule
import woowacourse.shopping.fake.FakeProductRepository
import woowacourse.shopping.otterdi.Injector
import woowacourse.shopping.otterdi.annotation.Inject
import woowacourse.shopping.otterdi.annotation.Qualifier

class InjectorTest {

    @Test
    fun `Inject annotation이 있는 프로퍼티에만 의존성을 주입한다`() {
        // given & when
        val injector = Injector(FakeModule)
        val vm = injector.inject<FakeViewModel>()

        // then
        assertNotNull(vm.productRepository)
        assertNotNull(vm.cartMultiRepository)
    }

    @Test
    fun `Inject annotation이 없는 프로퍼티는 초기화되지 않는다`() {
        // given
        val injector = Injector(FakeModule)
        val vm = injector.inject<FakeViewModel>()

        // then
        assertThrows<UninitializedPropertyAccessException> { vm.cartRepository }
    }

    @Test
    fun `하나의 인터페이스에 대해 구현체가 여러개인 경우에는 Qualifier로 구현체를 지정한다`() {
        // given
        val injector = Injector(FakeModule)
        val vm = injector.inject<FakeViewModel>()

        // then
        assertTrue(vm.cartMultiRepository is FakeDefaultCartRepository)
    }

    @Test
    fun `인스턴스화에 필요한 의존성 주입 모듈이 없다면 객체 생성에 실패한다`() {
        // given
        val injector = Injector()

        // when
        assertThrows<IllegalArgumentException> { injector.inject<FakeViewModel>() }
    }

    @Test
    fun `의존성 주입이 필요한 곳에 Inject 어노테이션을 붙이지 않았다면 객체 생성에 실패한다`() {
        // given
        val injector = Injector(FakeModule)

        // when
        assertThrows<IllegalArgumentException> { injector.inject<FakeViewModel2>() }
    }
}

class FakeViewModel(
    @Inject
    @Qualifier("FakeDefaultCartRepository")
    val cartMultiRepository: FakeCartRepository,
) {
    @Inject
    lateinit var productRepository: FakeProductRepository
    lateinit var cartRepository: FakeCartRepository
}

class FakeViewModel2(
    val productRepository: FakeProductRepository,
)
