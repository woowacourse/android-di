package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

class AoDiTest {
    @Test
    fun `생성자 의존성을 재귀적으로 생성한다`() {
        val root = AoDi.instantiate(Root::class)

        assertThat(root.branch.leaf).isInstanceOf(Leaf::class.java)
    }

    @Test
    fun `같은 타입의 의존성을 재사용한다`() {
        val first = AoDi.instantiate(FirstConsumer::class)
        val second = AoDi.instantiate(SecondConsumer::class)

        assertThat(first.dependency).isSameAs(second.dependency)
    }

    @Test
    fun `등록한 객체를 생성자 의존성으로 사용한다`() {
        val registeredDependency = object : RegisteredDependency {}

        AoDi.register(RegisteredDependency::class, registeredDependency)
        val consumer = AoDi.instantiate(RegisteredConsumer::class)

        assertThat(consumer.dependency).isSameAs(registeredDependency)
    }

    @Test
    fun `인터페이스 정보와 구현체 정보 연결 규칙을 설정하고 인터페이스를 불러도 구현체를 가져온다`() {
        AoDi.registerInterfaceRule(InterfaceClass::class, InterfaceImpl::class)

        val result = AoDi.instantiate(InterfaceClass::class)
        assertThat(result::class).isSameAs(InterfaceImpl::class)
    }

    @Test
    fun `뷰모델에 의존성 객체를 필드 주입한다`() {
        val viewModel = CartRepositoryFieldViewModel()

        val fakeRepository =
            object : CartRepository {
                override suspend fun addCartProduct(product: Product) {
                    TODO("Not yet implemented")
                }

                override suspend fun getAllCartProducts(): List<CartProduct> {
                    TODO("Not yet implemented")
                }

                override suspend fun deleteCartProduct(id: Long) {
                    TODO("Not yet implemented")
                }
            }

        AoDi.register(CartRepository::class, fakeRepository)
        AoDi.inject(viewModel)

        assertThat(viewModel.cartRepository).isSameAs(fakeRepository)
    }

    @Test
    fun `필드 의존성을 연결 규칙에 따라 생성하고 생성자 의존성을 재사용한다`() {
        val dataSource = FieldDataSource()
        AoDi.register(FieldDataSource::class, dataSource)
        AoDi.registerInterfaceRule(FieldRepository::class, DefaultFieldRepository::class)
        val viewModel = FieldInjectedViewModel()

        AoDi.inject(viewModel)

        assertThat(viewModel.repository).isInstanceOf(DefaultFieldRepository::class.java)
        assertThat((viewModel.repository as DefaultFieldRepository).dataSource).isSameAs(dataSource)
    }

    @Test
    fun `비공개 필드에도 등록된 의존성 객체를 주입한다`() {
        val dependency = PrivateFieldDependency()
        AoDi.register(PrivateFieldDependency::class, dependency)
        val viewModel = PrivateFieldViewModel()

        AoDi.inject(viewModel)

        assertThat(viewModel.hasSameDependency(dependency)).isTrue()
    }

    @Test
    fun `애노테이션이 붙은 필드만 주입한다`() {
        val dependency = PrivateFieldDependency()
        AoDi.register(PrivateFieldDependency::class, dependency)
        val viewModel = SelectiveFieldViewModel()

        AoDi.inject(viewModel)

        assertThat(viewModel.annotatedDependency).isSameAs(dependency)
        assertThat(viewModel.unannotatedDependency).isNull()
    }

    class Root(
        val branch: Branch,
    )

    class Branch(
        val leaf: Leaf,
    )

    class Leaf

    class FirstConsumer(
        val dependency: SharedDependency,
    )

    class SecondConsumer(
        val dependency: SharedDependency,
    )

    class SharedDependency

    interface RegisteredDependency

    class RegisteredConsumer(
        val dependency: RegisteredDependency,
    )

    interface InterfaceClass

    class InterfaceImpl : InterfaceClass

    class FieldDataSource

    interface FieldRepository

    class DefaultFieldRepository(
        val dataSource: FieldDataSource,
    ) : FieldRepository

    class FieldInjectedViewModel : ViewModel() {
        @FieldInject
        lateinit var repository: FieldRepository
    }

    class CartRepositoryFieldViewModel : ViewModel() {
        @FieldInject
        lateinit var cartRepository: CartRepository
    }

    class PrivateFieldDependency

    class PrivateFieldViewModel : ViewModel() {
        @FieldInject
        private lateinit var dependency: PrivateFieldDependency

        fun hasSameDependency(expected: PrivateFieldDependency): Boolean = dependency === expected
    }

    class SelectiveFieldViewModel : ViewModel() {
        @FieldInject
        var annotatedDependency: PrivateFieldDependency? = null

        var unannotatedDependency: PrivateFieldDependency? = null
    }
}
