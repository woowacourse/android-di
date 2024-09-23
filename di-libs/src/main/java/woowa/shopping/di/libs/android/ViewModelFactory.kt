package woowa.shopping.di.libs.android

import androidx.activity.ComponentActivity
import woowa.shopping.di.libs.annotation.InternalApi
import woowa.shopping.di.libs.container.Container
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.qualify.qualifier
import woowa.shopping.di.libs.scope.Scope
import woowa.shopping.di.libs.scope.ScopeDSL
import woowa.shopping.di.libs.scope.startScope

@OptIn(InternalApi::class)
inline fun <reified VM : ScopeViewModel> ComponentActivity.scopeViewModel(): Lazy<VM> {
    return lazy {
        startScope(qualifier<VM>())
        Containers.resolveScopedInstance(qualifier<VM>(), VM::class, null).also {
            lifecycle.addObserver(it)
        }
    }
}

inline fun <reified VM : ScopeViewModel> Container.viewModel(
    crossinline configureScope: ScopeDSL.() -> Unit = {},
    noinline viewModelFactory: Scope.() -> VM,
) {
    scope<VM> {
        scoped { viewModelFactory() }
        configureScope()
    }
}
