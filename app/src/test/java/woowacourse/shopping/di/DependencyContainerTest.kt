package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DependencyContainerTest {
    class TestRepository

    @Test
    fun `요청한 타입의 인스턴스를 생성한다`() {
        val container = DependencyContainer()

        val repository = container.resolve(TestRepository::class)

        assertThat(repository).isInstanceOf(TestRepository::class.java)
    }

    @Test
    fun `동일한 타입을 여러 번 요청하면 같은 인스턴스를 반환한다`() {
        val container = DependencyContainer()

        val repo1 = container.resolve(TestRepository::class)
        val repo2 = container.resolve(TestRepository::class)

        assertThat(repo1).isEqualTo(repo2)
    }
}
