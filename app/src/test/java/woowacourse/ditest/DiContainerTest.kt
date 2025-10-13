package woowacourse.ditest

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.Test
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.di.DiContainer
import woowacourse.shopping.di.annotation.MyInjector
import woowacourse.shopping.model.CartRepository

class DiContainerTest {
    @Test
    fun `repository가 생성된다`() {
        val cartRepository = DiContainer.getInstance(CartRepository::class)

        cartRepository.shouldBeInstanceOf<DefaultCartRepository>()
    }

    @Test
    fun `추상체든 구현체든 똑같은 인스턴스가 나온다`() {
        val cartRepository1 = DiContainer.getInstance(CartRepository::class)
        val cartRepository2 = DiContainer.getInstance(DefaultCartRepository::class)

        cartRepository1.shouldBeSameInstanceAs(cartRepository2)
    }

    @Test
    fun `기본 생성자가 없는 클래스는 예외가 발생한다`() {
        class NoPublicConstructor {
            constructor()
        }

        shouldThrow<IllegalStateException> {
            DiContainer.getInstance(NoPublicConstructor::class)
        }
    }

    @Test
    fun `지원하지 않는 파라미터 형식 클래스는 에러가 발생한다`() {
        class DisApplyClass(val callback: () -> Unit)

        shouldThrow<IllegalStateException> {
            DiContainer.getInstance(DisApplyClass::class)
        }
    }

    @Test
    fun `어노테이션을 붙이면 필드 주입이 가능하다`() {
        class DisApplyClass() {
            @MyInjector
            lateinit var cartRepository: CartRepository
        }

        val instance = DiContainer.getInstance(DisApplyClass::class)
        instance.cartRepository.shouldBeInstanceOf<DefaultCartRepository>()
    }

    @Test
    fun `어노테이션을 붙이면 필드 주입이 안된다`() {
        class DisApplyClass() {
            lateinit var cartRepository: CartRepository
        }

        val instance = DiContainer.getInstance(DisApplyClass::class)
        shouldThrow<UninitializedPropertyAccessException> {
            instance.cartRepository
        }
    }

    @Test
    fun `val값은 어노테이션을 붙이더라도 에러가 발생한다`() {
        class DisApplyClass(@MyInjector val cartRepository: CartRepository)

        shouldThrow<ClassCastException> {
            DiContainer.getInstance(DisApplyClass::class)
        }
    }
}