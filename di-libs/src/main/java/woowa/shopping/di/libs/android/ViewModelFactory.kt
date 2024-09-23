package woowa.shopping.di.libs.android

import androidx.activity.ComponentActivity
import woowa.shopping.di.libs.annotation.InternalApi
import woowa.shopping.di.libs.container.Container
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.factory.Lifecycle
import woowa.shopping.di.libs.qualify.qualifier
import woowa.shopping.di.libs.scope.Scope
import woowa.shopping.di.libs.scope.ScopeDSL

@OptIn(InternalApi::class)
inline fun <reified VM : ScopeViewModel> ComponentActivity.scopeViewModel(): Lazy<VM> {
    return lazy {
        Containers.resolveScopedInstance(qualifier<VM>(), VM::class, null).also {
            lifecycle.addObserver(it)
        }
    }
}

@OptIn(InternalApi::class)
inline fun <reified VM : ScopeViewModel> Container.viewModel(
    crossinline onRegister: ScopeDSL.() -> Unit = {},
    noinline viewModelFactory: Scope.() -> VM,
) {
    val scopeQualifier = qualifier<VM>()
    val scope = Scope(scopeQualifier, Lifecycle.SCOPED)
    scope<VM> {
        viewModelFactory(scope)
        onRegister()
    }
}
