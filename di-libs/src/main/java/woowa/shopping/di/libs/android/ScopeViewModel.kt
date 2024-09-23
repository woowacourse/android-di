package woowa.shopping.di.libs.android

import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import woowa.shopping.di.libs.qualify.Qualifier
import woowa.shopping.di.libs.qualify.qualifier
import woowa.shopping.di.libs.scope.ScopeComponent
import woowa.shopping.di.libs.scope.startScope

abstract class ScopeViewModel : ScopeComponent, DefaultLifecycleObserver {
    protected val viewModelScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    override val scope by lazy {
        startScope(qualifier(this::class))
    }

    inline fun <reified T : Any> get(qualifier: Qualifier? = null): T {
        return scope.get(qualifier)
    }

    inline fun <reified T : Any> inject(qualifier: Qualifier? = null): Lazy<T> {
        return scope.inject(qualifier)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (owner is ComponentActivity && owner.isChangingConfigurations) return
        onClear()
        viewModelScope.coroutineContext.cancelChildren()
    }
}