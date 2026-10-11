package woowacourse.shopping.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.di.DIContainer

class ScreenScopeTest {
    private val container =
        DIContainer().apply {
            bind(ScreenDependency::class, ScreenDependency::class, scopeType = ScreenScope)
        }
    private val appScopedContainer = container.openScope(AppScope, "test-application")

    @Test
    fun `같은 화면에서는 같은 인스턴스를 재사용한다`() {
        val store = ViewModelStore()
        val factory = ScreenScopeViewModelFactory(appScopedContainer, "cart")
        val scopeOwner = ViewModelProvider(store, factory)[ScreenScopeViewModel::class.java]

        val first = scopeOwner.resolver.get(ScreenDependency::class)
        val second = scopeOwner.resolver.get(ScreenDependency::class)

        assertThat(second).isSameInstanceAs(first)
    }

    @Test
    fun `화면 소유 ViewModel이 소멸하면 화면 스코프의 인스턴스를 제거한다`() {
        val store = ViewModelStore()
        val factory = ScreenScopeViewModelFactory(appScopedContainer, "cart")
        val scopeOwner = ViewModelProvider(store, factory)[ScreenScopeViewModel::class.java]
        val beforeClear = scopeOwner.resolver.get(ScreenDependency::class)

        store.clear()
        val reopenedScope = appScopedContainer.openScope(ScreenScope, "cart")
        val afterClear = reopenedScope.get(ScreenDependency::class)

        assertThat(afterClear).isNotSameInstanceAs(beforeClear)
    }

    class ScreenDependency
}
