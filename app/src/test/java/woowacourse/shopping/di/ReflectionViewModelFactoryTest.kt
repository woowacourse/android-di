package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ReflectionViewModelFactoryTest {
    class TestRepository

    class TestViewModel(
        val repository: TestRepository,
    ) : ViewModel()

    class TestService(
        val repository: TestRepository,
    )

    class TestAnotherViewModel(
        val testService: TestService,
    ) : ViewModel()

    @Test
    fun `ViewModel 생성자에 필요한 의존성을 자동으로 주입한다`() {
        val container = DependencyContainer()

        val factory = ReflectionViewModelFactory(container)

        val viewModel = factory.create(TestViewModel::class.java)
        val repository = container.resolve(TestRepository::class)

        assertThat(viewModel.repository).isEqualTo(repository)
    }

    @Test
    fun `새로운 viewModel도 factory 변경 없이 사용 가능하다`() {
        val container = DependencyContainer()

        val factory = ReflectionViewModelFactory(container)

        val viewModel = factory.create(TestAnotherViewModel::class.java)

        val repository = container.resolve(TestRepository::class)

        assertThat(viewModel.testService.repository).isEqualTo(repository)
    }
}
