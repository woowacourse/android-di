package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

inline fun <reified VM : ViewModel> viewModelInject(): ViewModelProvider.Factory {
    val repository =
        VM::class
            .primaryConstructor
            ?.parameters
            ?: throw NullPointerException("No Primary Constructor Found")

    val instances =
        repository.map {
            val type: KClass<*> = it.type.jvmErasure
            RepositoryContainer.getInstance(type)
        }

    return viewModelFactory {
        initializer {
            VM::class.primaryConstructor!!.call(*instances.toTypedArray())
        }
    }
}
