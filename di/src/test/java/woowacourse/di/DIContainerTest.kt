package woowacourse.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DIContainerTest {
    class ProductRepository

    class CartRepository

    @BeforeEach
    fun setup() {
        DIContainer.register(ProductRepository::class) { ProductRepository() }
    }

    @Test
    fun `register와 get이 올바르게 동작한다`() {
        val repo1 = DIContainer.get(ProductRepository::class)
        val repo2 = DIContainer.get(ProductRepository::class)

        assertThat(repo1 === repo2).isTrue()
    }

    @Test
    fun `등록되지 않은 타입을 get 하면 예외가 발생한다`() {
        assertThrows<Exception> {
            DIContainer.get(CartRepository::class)
        }
    }
}
