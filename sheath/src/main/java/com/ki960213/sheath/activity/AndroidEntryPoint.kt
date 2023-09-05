package com.ki960213.sheath.activity

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.ki960213.sheath.container.SingletonContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

abstract class AndroidEntryPoint : AppCompatActivity() {

    @MainThread
    inline fun <reified VM : ViewModel> ComponentActivity.viewModels(
        noinline extrasProducer: (() -> CreationExtras)? = null,
        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null,
    ): Lazy<VM> {
        val factoryPromise = factoryProducer ?: { defaultViewModelProviderFactory }

        val primaryConstructor = VM::class.primaryConstructor ?: return ViewModelLazy(
            VM::class,
            { viewModelStore },
            factoryPromise,
            { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
        )

        @Suppress("UNCHECKED_CAST")
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(VM::class.java)) {
                    val args = primaryConstructor.parameters.map {
                        (application as SingletonContainer).getInstance(it.type.classifier as KClass<*>)
                    }
                    return primaryConstructor.call(*args.toTypedArray()) as T
                }
                throw IllegalArgumentException("unknown viewModel class")
            }
        }

        return ViewModelLazy(
            VM::class,
            { viewModelStore },
            { viewModelFactory },
            { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
        )
    }
}
