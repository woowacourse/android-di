package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.DependencyContainer

class DependencyContainerTest {
    @Test
    fun `ProductRepository 의존성 주입 테스트`() {
        // when
        val dependency =
            DependencyContainer.get(ProductRepository::class)

        // then
        assertThat(dependency)
            .isInstanceOf(ProductRepository::class.java)
    }

    @Test
    fun `CartRepository를 여러 번 조회하면 같은 객체를 반환한다`() {
        // when
        val first =
            DependencyContainer.get(CartRepository::class)
        val second =
            DependencyContainer.get(CartRepository::class)

        // then
        assertThat(first).isSameAs(second)
    }

    @Test
    fun `등록하지 않은 타입을 조회하면 예외가 발생한다`() {
        assertThatThrownBy {
            DependencyContainer.get(String::class)
        }.isInstanceOf(IllegalStateException::class.java)
    }
}
