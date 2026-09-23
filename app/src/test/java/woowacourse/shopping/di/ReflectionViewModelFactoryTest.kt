package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.di.DependencyContainer
import woowacourse.di.Inject

@RunWith(RobolectricTestRunner::class)
class ReflectionViewModelFactoryTest {
    @Test
    fun `애노테이션이 붙은 필드에만 의존성을 재귀적으로 주입한다`() {
        val container =
            DependencyContainer(
                bindings = mapOf(TestRepository::class to DefaultTestRepository::class),
            )
        val factory = ReflectionViewModelFactory(container)

        val viewModel = factory.create(FieldInjectionViewModel::class.java)
        val anotherViewModel = factory.create(FieldInjectionViewModel::class.java)

        assertThat(viewModel.repository).isInstanceOf(DefaultTestRepository::class.java)
        assertThat(viewModel.repository.dependency).isNotNull()
        assertThat(anotherViewModel.repository).isSameInstanceAs(viewModel.repository)
        assertThat(viewModel.isIgnoredRepositoryInitialized()).isFalse()
    }

    private interface TestRepository {
        val dependency: TestDependency
    }

    private class DefaultTestRepository(
        override val dependency: TestDependency,
    ) : TestRepository

    private class TestDependency

    private class FieldInjectionViewModel : ViewModel() {
        @Inject
        private lateinit var injectedRepository: TestRepository

        private lateinit var ignoredRepository: TestRepository

        val repository: TestRepository get() = injectedRepository

        fun isIgnoredRepositoryInitialized(): Boolean = ::ignoredRepository.isInitialized
    }
}
