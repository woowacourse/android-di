package woowacourse.shopping.di

import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Test

class DIContainerScopeTest {
    private object TestScope : ScopeType
    private object AppScope : ScopeType
    private object ViewModelScope : ScopeType

    private val firstScopeKey = ScopeKey(TestScope, "first")
    private val secondScopeKey = ScopeKey(TestScope, "second")
    private val dependencyKey = DependencyKey(TestDependency::class)
    private val diContainer = DIContainer()

    @Test
    fun `같은 스코프에서는 인스턴스를 재사용한다`() {
        diContainer.openScope(firstScopeKey)

        val first = diContainer.resolve(dependencyKey, firstScopeKey)
        val second = diContainer.resolve(dependencyKey, firstScopeKey)

        assertSame(first, second)
    }

    @Test
    fun `다른 스코프에서는 다른 인스턴스를 생성한다`() {
        diContainer.openScope(firstScopeKey)
        diContainer.openScope(secondScopeKey)

        val first = diContainer.resolve(dependencyKey, firstScopeKey)
        val second = diContainer.resolve(dependencyKey, secondScopeKey)

        assertNotSame(first, second)
    }

    @Test
    fun `스코프를 닫고 다시 열면 새 인스턴스를 생성한다`() {
        diContainer.openScope(firstScopeKey)
        val first = diContainer.resolve(dependencyKey, firstScopeKey)

        diContainer.closeScope(firstScopeKey)
        assertThrows(IllegalStateException::class.java) {
            diContainer.resolve(dependencyKey, firstScopeKey)
        }

        diContainer.openScope(firstScopeKey)
        val second = diContainer.resolve(dependencyKey, firstScopeKey)

        assertNotSame(first, second)
    }

    @Test
    fun `ViewModel 스코프마다 인스턴스를 분리하고 앱 스코프 인스턴스는 공유한다`() {
        val appKey = ScopeKey(AppScope, "app")
        val firstViewModelKey = ScopeKey(ViewModelScope, "first")
        val secondViewModelKey = ScopeKey(ViewModelScope, "second")
        diContainer.openScope(appKey)
        diContainer.openScope(firstViewModelKey, parentKey = appKey)
        diContainer.openScope(secondViewModelKey, parentKey = appKey)
        diContainer.bind(AppDependency::class, AppDependency::class, scopeType = AppScope)
        diContainer.bind(ViewModelDependency::class, ViewModelDependency::class, scopeType = ViewModelScope)

        val firstAppDependency = diContainer.resolve(DependencyKey(AppDependency::class), firstViewModelKey)
        val secondAppDependency = diContainer.resolve(DependencyKey(AppDependency::class), secondViewModelKey)
        val firstViewModelDependency = diContainer.resolve(DependencyKey(ViewModelDependency::class), firstViewModelKey)
        val secondViewModelDependency = diContainer.resolve(DependencyKey(ViewModelDependency::class), secondViewModelKey)

        assertSame(firstAppDependency, secondAppDependency)
        assertNotSame(firstViewModelDependency, secondViewModelDependency)
    }

    class TestDependency
    class AppDependency
    class ViewModelDependency
}
