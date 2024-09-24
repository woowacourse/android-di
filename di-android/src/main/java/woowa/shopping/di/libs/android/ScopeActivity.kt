package woowa.shopping.di.libs.android

import androidx.appcompat.app.AppCompatActivity
import woowa.shopping.di.libs.qualify.Qualifier
import woowa.shopping.di.libs.qualify.qualifier
import woowa.shopping.di.libs.scope.ScopeComponent
import woowa.shopping.di.libs.scope.startScope

abstract class ScopeActivity : AppCompatActivity(), ScopeComponent {

    override val scope by lazy {
        startScope(qualifier(this::class))
    }

    inline fun <reified T : Any> get(qualifier: Qualifier? = null): T {
        return scope.get(qualifier)
    }

    inline fun <reified T : Any> inject(qualifier: Qualifier? = null): Lazy<T> {
        return scope.inject(qualifier)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isChangingConfigurations) return
        onClear()
    }
}