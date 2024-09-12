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
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class ViewModelFactory<VM : ViewModel>() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: error("ViewModel 에 주 생성자가 없습니다. ViewModel: $modelClass")
        val params = constructor.parameters.map { param ->
            val kClass = param.type.jvmErasure
            Containers.resolve(kClass)
        }.toTypedArray()
        return constructor.call(*params)
    }
}

inline fun <reified VM : ViewModel> ComponentActivity.injectViewModel(): Lazy<VM> {
    return viewModels {
        Containers.resolve<ViewModelFactory<*>>(
            ViewModelFactory::class,
            qualifier = qualifier<VM>(),
            lifecycle = Lifecycle.SINGLETON
        )
    }
}

inline fun <reified VM : ViewModel> Container.viewModel() {
    val qualifier = qualifier<VM>()
    instanceRegistry[Key(ViewModelFactory::class, qualifier, Lifecycle.SINGLETON)] =
        SingletonInstanceFactory(
            qualifier,
            factory = { ViewModelFactory<VM>() }
        )
}