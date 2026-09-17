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
}
