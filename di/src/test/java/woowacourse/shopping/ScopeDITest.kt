package woowacourse.shopping

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import shopping.di.DIContainer
import shopping.di.Scope

class ScopeDITest {

    @Before
    fun setUp() {
        DIContainer.clearAllScopes()
    }

    @Test
    fun `앱 스코프에서 의존성이 싱글톤으로 유지되는지 테스트`() {
        DIContainer.register(ScopedService::class.java, AppScopedService(), scope = Scope.APP)

        val instance1 = DIContainer.resolve(ScopedService::class.java, scope = Scope.APP)
        val instance2 = DIContainer.resolve(ScopedService::class.java, scope = Scope.APP)

        assertSame(instance1, instance2)
    }
    @Test
    fun `Activity 스코프에서 의존성이 스코프 클리어 후 재생성되는지 테스트`() {
        DIContainer.register(
            ScopedService::class.java,
            ActivityScopedService(),
            scope = Scope.ACTIVITY
        )

        val instance1 = DIContainer.resolve(ScopedService::class.java, scope = Scope.ACTIVITY)
        DIContainer.clearActivityScope()

        DIContainer.register(
            ScopedService::class.java,
            ActivityScopedService(),
            scope = Scope.ACTIVITY
        )

        val instance2 = DIContainer.resolve(ScopedService::class.java, scope = Scope.ACTIVITY)

        assertNotSame(instance1, instance2)
    }
    @Test
    fun `ViewModel 스코프에서 의존성이 스코프 클리어 후 재생성되는지 테스트`() {
        DIContainer.register(
            ScopedService::class.java,
            ViewModelScopedService(),
            scope = Scope.VIEWMODEL
        )

        val instance1 = DIContainer.resolve(ScopedService::class.java, scope = Scope.VIEWMODEL)
        DIContainer.clearViewModelScope()

        DIContainer.register(
            ScopedService::class.java,
            ViewModelScopedService(),
            scope = Scope.VIEWMODEL
        )

        val instance2 = DIContainer.resolve(ScopedService::class.java, scope = Scope.VIEWMODEL)

        assertNotSame(instance1, instance2)
    }

    @Test
    fun `ScopedTestClass의 의존성이 올바르게 주입되는지 테스트`() {
        DIContainer.register(ScopedService::class.java, AppScopedService(), scope = Scope.APP)
        DIContainer.register(
            ScopedService::class.java,
            ActivityScopedService(),
            scope = Scope.ACTIVITY
        )
        DIContainer.register(
            ScopedService::class.java,
            ViewModelScopedService(),
            scope = Scope.VIEWMODEL
        )

        val scopedTestClass = DIContainer.resolve(ScopedTestClass::class.java)

        assertEquals("App Scoped Service", scopedTestClass.appScopedService.getScopeMessage())
        assertEquals(
            "Activity Scoped Service",
            scopedTestClass.activityScopedService.getScopeMessage()
        )
        assertEquals(
            "ViewModel Scoped Service",
            scopedTestClass.viewModelScopedService.getScopeMessage()
        )
    }
}
