package woowacourse.ditest

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.Test
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.di.DiContainer
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
}