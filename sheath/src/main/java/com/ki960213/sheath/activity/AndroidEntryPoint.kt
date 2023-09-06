package com.ki960213.sheath.activity

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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

        val viewModelFactory = viewModelFactory {
            initializer {
                val args = primaryConstructor.parameters.map {
                    (this[APPLICATION_KEY] as SingletonContainer).getInstance(it.type.classifier as KClass<*>)
                }
                primaryConstructor.call(*args.toTypedArray())
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
