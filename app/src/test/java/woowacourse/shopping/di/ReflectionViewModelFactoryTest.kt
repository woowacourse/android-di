package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.di.DependencyContainer
import woowacourse.di.MyInject
import woowacourse.di.Scope
import woowacourse.di.ScopeType

@RunWith(RobolectricTestRunner::class)
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

    class TestFieldInjectionViewModel : ViewModel() {
        @MyInject
        lateinit var repository: TestRepository
    }

    class TestFailingConstructorViewModel(
        val repository: TestRepository,
    ) : ViewModel() {
        init {
            error("ViewModel 생성 실패")
        }
    }

    interface MissingRepository

    class TestFailingFieldInjectionViewModel(
        val repository: TestRepository,
    ) : ViewModel() {
        @MyInject
        lateinit var missingRepository: MissingRepository
    }

    @Test
    fun `ViewModel 생성자에 필요한 의존성을 자동으로 주입한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        container.bind(TestRepository::class, app)
        val appScope = container.openScope(app, "application")
        val factory = ReflectionViewModelFactory(container, appScope, ScopeType("viewModel"))

        val viewModel = factory.create(TestViewModel::class.java)
        val repository = appScope.resolve(TestRepository::class)

        assertThat(viewModel.repository).isSameAs(repository)
    }

    @Test
    fun `새로운 viewModel도 factory 변경 없이 사용 가능하다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        container.bind(TestRepository::class, app)
        container.bind(TestService::class, app)
        val appScope = container.openScope(app, "application")
        val factory = ReflectionViewModelFactory(container, appScope, ScopeType("viewModel"))

        val viewModel = factory.create(TestAnotherViewModel::class.java)

        val repository = appScope.resolve(TestRepository::class)

        assertThat(viewModel.testService.repository).isSameAs(repository)
    }

    @Test
    fun `ViewModel은 매번 생성하지만 앱 의존성은 재사용한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        container.bind(TestRepository::class, app)
        val appScope = container.openScope(app, "application")
        val factory = ReflectionViewModelFactory(container, appScope, ScopeType("viewModel"))

        val viewModel1 = factory.create(TestViewModel::class.java)
        val viewModel2 = factory.create(TestViewModel::class.java)

        assertThat(viewModel1).isNotSameAs(viewModel2)
        assertThat(viewModel1.repository).isSameAs(viewModel2.repository)
    }

    @Test
    fun `ViewModel을 생성한 후 어노테이션이 붙은 필드에 의존성을 주입한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        container.bind(TestRepository::class, app)
        val appScope = container.openScope(app, "application")
        val factory = ReflectionViewModelFactory(container, appScope, ScopeType("viewModel"))

        val viewModel = factory.create(TestFieldInjectionViewModel::class.java)
        val repository = appScope.resolve(TestRepository::class)

        assertThat(viewModel.repository).isSameAs(repository)
    }

    @Test
    fun `ViewModelStore를 비우면 해당 스코프만 닫고 앱과 다른 ViewModel 스코프는 유지한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        val viewModelScopeType = ScopeType("viewModel")
        container.bind(TestRepository::class, app)
        container.bind(TestRepository::class, viewModelScopeType)
        val appScope = container.openScope(app, "application")
        val appRepository = appScope.resolve(TestRepository::class)
        val factory = ReflectionViewModelFactory(container, appScope, viewModelScopeType)
        val firstStore = ViewModelStore()
        val secondStore = ViewModelStore()
        val firstProvider = ViewModelProvider(firstStore, factory)
        val firstViewModel = firstProvider[TestViewModel::class.java]
        val secondViewModel = ViewModelProvider(secondStore, factory)[TestViewModel::class.java]
        val firstScope = requireNotNull(firstViewModel.getCloseable<Scope>(ReflectionViewModelFactory.SCOPE_KEY))
        val secondScope = requireNotNull(secondViewModel.getCloseable<Scope>(ReflectionViewModelFactory.SCOPE_KEY))
        assertThat(firstProvider[TestViewModel::class.java]).isSameAs(firstViewModel)
        assertThat(firstScope.resolve(TestRepository::class)).isSameAs(firstViewModel.repository)

        firstStore.clear()

        assertThatThrownBy {
            firstScope.resolve(TestRepository::class)
        }.isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("닫힌 스코프")
        assertThat(appScope.resolve(TestRepository::class)).isSameAs(appRepository)
        assertThat(secondScope.resolve(TestRepository::class)).isSameAs(secondViewModel.repository)
        secondStore.clear()
    }

    @Test
    fun `ViewModel 생성이나 필드 주입에 실패해도 새 스코프는 남지 않는다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        val viewModelScopeType = ScopeType("viewModel")
        container.bind(TestRepository::class, app)
        container.bind(TestRepository::class, viewModelScopeType)
        val appScope = container.openScope(app, "application")
        val appRepository = appScope.resolve(TestRepository::class)
        val factory = ReflectionViewModelFactory(container, appScope, viewModelScopeType)
        val store = ViewModelStore()
        val viewModel = ViewModelProvider(store, factory)[TestViewModel::class.java]
        val scope = requireNotNull(viewModel.getCloseable<Scope>(ReflectionViewModelFactory.SCOPE_KEY))
        assertThat(container.activeScopeCount).isEqualTo(2)

        assertThatThrownBy {
            factory.create(TestFailingConstructorViewModel::class.java)
        }.hasRootCauseInstanceOf(IllegalStateException::class.java)
            .hasRootCauseMessage("ViewModel 생성 실패")
        assertThat(container.activeScopeCount).isEqualTo(2)

        assertThatThrownBy {
            factory.create(TestFailingFieldInjectionViewModel::class.java)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining(MissingRepository::class.qualifiedName)
        assertThat(container.activeScopeCount).isEqualTo(2)
        assertThat(appScope.resolve(TestRepository::class)).isSameAs(appRepository)
        assertThat(scope.resolve(TestRepository::class)).isSameAs(viewModel.repository)

        store.clear()
        assertThat(container.activeScopeCount).isEqualTo(1)
    }
}
