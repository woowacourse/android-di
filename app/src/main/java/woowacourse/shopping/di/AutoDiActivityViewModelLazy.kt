package woowacourse.shopping.di

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.autoViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
): Lazy<VM> {
    val factory = viewModelFactory {
        initializer {
            VM::class.primaryConstructor?.let{ constructor ->
                val parameters = constructor.parameters
                val arguments = parameters
                    .filter { !it.isOptional }
                    .associateWith { parameter -> containerProvider(parameter) }
                constructor.callBy(arguments)
            }?: VM::class.createInstance()
        }
    }

    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { factory },
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras }
    )
}