package woowa.shopping.di.libs.android

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowa.shopping.di.libs.container.Container
import woowa.shopping.di.libs.container.Container.Key
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.factory.Lifecycle
import woowa.shopping.di.libs.factory.SingletonInstanceFactory
import woowa.shopping.di.libs.qualify.qualifier
import woowa.shopping.di.libs.scope.Scope

class ViewModelFactory<VM : ViewModel>(
    private val creator: () -> VM,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = creator() as T
}

inline fun <reified VM : ViewModel> ComponentActivity.injectViewModel(): Lazy<VM> {
    return viewModels {
        Containers.resolve<ViewModelFactory<*>>(
            ViewModelFactory::class,
            qualifier = qualifier<VM>(),
            lifecycle = Lifecycle.SINGLETON,
        )
    }
}

inline fun <reified VM : ViewModel> Container.viewModel(noinline factory: Scope.() -> VM) {
    val qualifier = qualifier<VM>()
    val scope = Scope(qualifier, lifecycle = Lifecycle.SINGLETON)
    instanceRegistry[Key(ViewModelFactory::class, qualifier, Lifecycle.SINGLETON)] =
        SingletonInstanceFactory(
            qualifier,
            factory = { ViewModelFactory { factory(scope) } },
        )
}
