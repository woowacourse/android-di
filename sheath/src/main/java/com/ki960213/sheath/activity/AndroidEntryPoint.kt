package com.ki960213.sheath.activity

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.ki960213.sheath.SheathApplication
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
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

        val viewModelFactory = createViewModelFactory<VM>(primaryConstructor)

        return ViewModelLazy(
            VM::class,
            { viewModelStore },
            { viewModelFactory },
            { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
        )
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified VM : ViewModel> createViewModelFactory(primaryConstructor: KFunction<*>): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(VM::class.java)) {
                    val args = getArgsFromApplicationContainer(primaryConstructor)
                    return primaryConstructor.call(*args.toTypedArray()) as T
                }
                throw IllegalArgumentException("unknown viewModel class")
            }
        }

    fun getArgsFromApplicationContainer(function: KFunction<*>) = function.parameters.map {
        val instances = (application as SheathApplication).container
            .filter { element -> (it.type.classifier as KClass<*>).isInstance(element) }
        when {
            instances.isEmpty() -> throw IllegalStateException("Application container에 ${function.name} 함수의 ${it.type} 타입의 인스턴스가 존재하지 않습니다.")
            instances.size >= 2 -> throw IllegalStateException("Application container에 ${function.name} 함수의 ${it.type} 타입의 인스턴스가 두 개 이상 존재합니다.")
        }
        instances[0]
    }
}
