package woowa.shopping.di.libs.scope

interface ScopeComponent {
    val scope: Scope

    fun onClear() {
        scope.cancel()
    }
}