package woowa.shopping.di.libs.android

import androidx.activity.ComponentActivity
import woowa.shopping.di.libs.annotation.InternalApi
import woowa.shopping.di.libs.container.Container
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.qualify.qualifier
import woowa.shopping.di.libs.scope.Scope
import woowa.shopping.di.libs.scope.ScopeDSL
import woowa.shopping.di.libs.scope.isLocked
import woowa.shopping.di.libs.scope.startScope

@OptIn(InternalApi::class)
inline fun <reified VM : ScopeViewModel> ComponentActivity.scopeViewModel(): Lazy<VM> {
    return lazy {
        val scopeQualifier = qualifier<VM>()
        if (isLocked(scopeQualifier)) {
            startScope(scopeQualifier)
        }
        Containers.resolveScopedInstance(scopeQualifier, VM::class, null).also {
            lifecycle.addObserver(it)
        }
    }
}

@OptIn(InternalApi::class)
inline fun <reified VM : ScopeViewModel> ComponentActivity.getViewModel(): VM {
    check(Containers.scopeInstanceRegistry.isLocked(qualifier<VM>()).not()) {
        "Scope 이 초기화 되지 않았습니다."
    }
    return Containers.resolveScopedInstance(qualifier<VM>(), VM::class, null)
}

/**
 * [ScopeViewModel] 을 생성하고, [Container] 에 등록합니다.
 *
 * @param configureBindings [ScopeViewModel] 생명주기를 따르는 객체를 바인딩하는 작업
 * @param viewModelFactory [Scope] 에서 사용할 [ScopeViewModel] 을 생성합니다.
 */
inline fun <reified VM : ScopeViewModel> Container.viewModel(
    crossinline configureBindings: ScopeDSL.() -> Unit = {},
    noinline viewModelFactory: Scope.() -> VM,
) {
    scope<VM> {
        scoped { viewModelFactory() }
        configureBindings()
    }
}
