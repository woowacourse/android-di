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

    class InjectTestViewModel(
        @Inject
        @Qualifier("MultiImplCartRepositoryDefault")
        val multiImplCartRepositoryDefault: FakeCartRepository,
        @Inject
        @Qualifier("MultiImplCartRepositorySecond")
        val multiImplCartRepository2: FakeCartRepository,
    ) {
        @Inject
        lateinit var productRepository: FakeProductRepository
        lateinit var cartRepository: FakeCartRepository
    }

    @Test
    fun `Inject annotation이 있는 프로퍼티에만 의존성을 주입한다`() {
        // given & when
        val injector = Injector(FakeModule)
        val vm = injector.inject<InjectTestViewModel>()

        // then
        assertNotNull(vm.productRepository)
        assertNotNull(vm.multiImplCartRepositoryDefault)
    }

    @Test
    fun `Inject annotation이 없는 프로퍼티는 초기화되지 않는다`() {
        // given
        val injector = Injector(FakeModule)
        val vm = injector.inject<InjectTestViewModel>()

        // then
        assertThrows<UninitializedPropertyAccessException> { vm.cartRepository }
    }

    @Test
    fun `하나의 인터페이스에 대해 구현체가 여러개인 경우에는 Qualifier로 구현체를 구분한다`() {
        // given
        val injector = Injector(FakeModule)
        val vm = injector.inject<InjectTestViewModel>()

        println("otter, ${vm.multiImplCartRepositoryDefault}")
        println("otter, ${vm.multiImplCartRepositoryDefault::class.simpleName}")
        println("otter, ${vm.multiImplCartRepository2::class.simpleName}")
        // then
        assertTrue(vm.multiImplCartRepositoryDefault is FakeDefaultCartRepository)
    }

    @Test
    fun `인스턴스화에 필요한 의존성 주입 모듈이 없다면 객체 생성에 실패한다`() {
        // given
        val injector = Injector()

        // when
        assertThrows<IllegalArgumentException> { injector.inject<InjectTestViewModel>() }
    }
}
