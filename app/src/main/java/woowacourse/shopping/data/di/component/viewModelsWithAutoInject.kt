package woowacourse.shopping.data.di.component

import androidx.annotation.MainThread
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModelsWithAutoInject(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = { inject() }
): Lazy<VM> {
    return viewModels(
        extrasProducer = extrasProducer,
        factoryProducer = factoryProducer
    )
}
