package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

class InjectorTest {
    @Test
    fun `애노테이션이 붙은 필드만 주입한다`() {
        val target = Injector.create(FieldTarget::class)

        assertThat(target.dependency).isInstanceOf(Dependency::class.java)
        assertThat(target.untouched).isSameAs(target.original)
        assertThat(target.isIgnoredInitialized()).isFalse()
    }

    @Test
    fun `상속받은 비공개 필드에도 주입한다`() {
        val target = Injector.create(ChildTarget::class)

        assertThat(target.inheritedDependency()).isInstanceOf(Dependency::class.java)
    }

    @Test
    fun `필드 의존성의 생성자 의존성까지 재귀적으로 주입한다`() {
        val target = Injector.create(RecursiveTarget::class)

        assertThat(target.dependency.dependency).isInstanceOf(Dependency::class.java)
    }

    @Test
    fun `변경할 수 없는 필드는 주입하지 않고 오류를 알린다`() {
        assertThatThrownBy { Injector.create(ImmutableTarget::class) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("변경 가능한 인스턴스 필드")
    }

    @Test
    fun `두 ViewModel의 필드에 동일한 Repository를 주입한다`() {
        val products = ViewModelFactory.create(ProductsViewModel::class.java)
        val cart = ViewModelFactory.create(CartViewModel::class.java)

        assertThat(products.cartRepository).isSameAs(cart.cartRepository)
        products.getAllProducts()
        assertThat(products.uiState.value.products).hasSize(3)
        assertThat(cart.uiState.value.cartProducts).isEmpty()
    }

    class Dependency

    class FieldTarget {
        val original = Dependency()
        var untouched = original
        lateinit var ignored: Dependency

        @Inject
        lateinit var dependency: Dependency

        fun isIgnoredInitialized(): Boolean = this::ignored.isInitialized
    }

    open class ParentTarget {
        @Inject
        private lateinit var dependency: Dependency

        fun inheritedDependency(): Dependency = dependency
    }

    class ChildTarget : ParentTarget()

    class ConstructorDependency(
        val dependency: Dependency,
    )

    class RecursiveTarget {
        @Inject
        lateinit var dependency: ConstructorDependency
    }

    class ImmutableTarget {
        @Inject
        val dependency = Dependency()
    }
}
