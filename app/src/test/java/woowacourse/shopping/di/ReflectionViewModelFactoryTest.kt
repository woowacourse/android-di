package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.di.DependencyContainer
import woowacourse.di.DependencyKey
import woowacourse.di.Inject
import woowacourse.di.ScopeType

@RunWith(RobolectricTestRunner::class)
class ReflectionViewModelFactoryTest {
    @Test
    fun `앱 의존성은 공유하고 ViewModel 의존성은 ViewModel마다 생성한다`() {
        val app = ScopeType("app")
        val viewModel = ScopeType("viewModel")
        val container =
            DependencyContainer(
                bindings = mapOf(DependencyKey(TestRepository::class) to DefaultTestRepository::class),
                scopes =
                    mapOf(
                        DependencyKey(TestRepository::class) to app,
                        DependencyKey(ViewModelDependency::class) to viewModel,
                    ),
            )
        val factory = ReflectionViewModelFactory(container.openScope(app), viewModel)
        val store = ViewModelStore()
        val provider = ViewModelProvider(store, factory)

        val first = provider.get("first", FieldInjectionViewModel::class.java)
        val second = provider.get("second", FieldInjectionViewModel::class.java)

        assertThat(first.repository).isInstanceOf(DefaultTestRepository::class.java)
        assertThat(first.repository.dependency).isNotNull()
        assertThat(second.repository).isSameInstanceAs(first.repository)
        assertThat(second.viewModelDependency).isNotSameInstanceAs(first.viewModelDependency)
        assertThat(first.isIgnoredRepositoryInitialized()).isFalse()

        store.clear()
        val recreated = ViewModelProvider(store, factory).get("first", FieldInjectionViewModel::class.java)
        assertThat(recreated).isNotSameInstanceAs(first)
        assertThat(recreated.viewModelDependency).isNotSameInstanceAs(first.viewModelDependency)
        assertThat(recreated.repository).isSameInstanceAs(first.repository)
    }

    private interface TestRepository {
        val dependency: TestDependency
    }

    private class DefaultTestRepository(
        override val dependency: TestDependency,
    ) : TestRepository

    private class TestDependency

    private class ViewModelDependency

    private class FieldInjectionViewModel : ViewModel() {
        @Inject
        private lateinit var injectedRepository: TestRepository

        @Inject
        private lateinit var injectedViewModelDependency: ViewModelDependency

        private lateinit var ignoredRepository: TestRepository

        val repository: TestRepository get() = injectedRepository

        val viewModelDependency: ViewModelDependency get() = injectedViewModelDependency

        fun isIgnoredRepositoryInitialized(): Boolean = ::ignoredRepository.isInitialized
    }
}
