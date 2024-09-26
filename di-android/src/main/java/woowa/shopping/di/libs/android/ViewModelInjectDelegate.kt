package woowa.shopping.di.libs.android

import woowa.shopping.di.libs.factory.Lifecycle
import woowa.shopping.di.libs.inject.inject
import woowa.shopping.di.libs.qualify.Qualifier
import woowa.shopping.di.libs.qualify.qualifier


inline fun <reified T : ScopeViewModel> injectViewModel(
    instanceQualifier: Qualifier? = null,
): Lazy<T> {
    return inject(instanceQualifier, qualifier<T>(), Lifecycle.SCOPED)
}