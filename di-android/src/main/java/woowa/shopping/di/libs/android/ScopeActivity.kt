package woowa.shopping.di.libs.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import woowa.shopping.di.libs.qualify.Qualifier
import woowa.shopping.di.libs.qualify.qualifier
import woowa.shopping.di.libs.scope.Scope
import woowa.shopping.di.libs.scope.ScopeComponent
import woowa.shopping.di.libs.scope.startScope

abstract class ScopeActivity : AppCompatActivity(), ScopeComponent {
    var _scope: Scope? = null
    override val scope: Scope
        get() = requireNotNull(_scope) { "Scope is not initialized" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (_scope != null) return
        _scope = startScope(qualifier(this::class))
    }

    inline fun <reified T : Any> get(qualifier: Qualifier? = null): T {
        return scope.get(qualifier)
    }

    inline fun <reified T : Any> inject(qualifier: Qualifier? = null): Lazy<T> {
        return lazy { scope.get(qualifier) }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isChangingConfigurations) return
        onClear()
        _scope = null
    }
}
