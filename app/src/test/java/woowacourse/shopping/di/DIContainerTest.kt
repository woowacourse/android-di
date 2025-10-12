package woowacourse.shopping.di

import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.fake.FakeProductRepository

class DIContainerTest {
    @Before
    fun setup() {
        DIContainer.register(ProductRepository::class) { FakeProductRepository() }
    }

    @Test
    fun `register와 get이 올바르게 동작한다`() {
        val repo1 = DIContainer.get(ProductRepository::class)
        val repo2 = DIContainer.get(ProductRepository::class)

        Assertions.assertThat(repo1 === repo2).isTrue()
    }

    @Test(expected = Exception::class)
    fun `등록되지 않은 타입을 get 하면 예외가 발생한다`() {
        DIContainer.get(CartRepository::class)
    }
}
