package woowacourse.ditest

import org.junit.Test
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.di.DiContainer
import woowacourse.shopping.model.CartRepository

class DiContainerTest {
    @Test
    fun `repository가 생성된다`() {
        val cartRepository = DiContainer.getInstance(CartRepository::class)

        assert(cartRepository is DefaultCartRepository)
    }

    @Test
    fun `추상체든 구현체든 똑같은 인스턴스가 나온다`() {
        val cartRepository1 = DiContainer.getInstance(CartRepository::class)
        val cartRepository2 = DiContainer.getInstance(DefaultCartRepository::class)

        assert(cartRepository1 === cartRepository2)
    }
}