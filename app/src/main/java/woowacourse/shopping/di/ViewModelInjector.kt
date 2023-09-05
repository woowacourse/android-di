package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object ViewModelInjector {
    inline fun <reified T : ViewModel> getInjectedViewModelFactory(): ViewModelProvider.Factory {
        val constructor = T::class.primaryConstructor
        requireNotNull(constructor) { "Unknown ViewModel Class ${T::class}" }

        val instances = constructor.parameters.map {
            Container.getInstance(it.type.jvmErasure)
        }

        return viewModelFactory {
            initializer {
                constructor.call(*instances.toTypedArray())
            }
        }
    }
}
