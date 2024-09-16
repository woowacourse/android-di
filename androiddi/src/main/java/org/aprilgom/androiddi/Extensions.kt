package org.aprilgom.androiddi

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun module(block: ModuleBuilder.() -> Unit): ModuleBuilder {
    val moduleBuilder = ModuleBuilder()
    moduleBuilder.block()
    return moduleBuilder
}

fun diContainer(block: DIContainerBuilder.() -> Unit) {
    val diContainer = DIContainerBuilder().apply(block).build()
    GlobalContext.register(diContainer)
}

fun DIContainerBuilder.modules(vararg moduleBuilders: ModuleBuilder) {
    this.moduleBuilders = moduleBuilders.toList()
}

fun DIContainerBuilder.androidContext(context: Context) {
    modules(
        module {
            single("AndroidContext") { context }
        },
    )
}

inline fun <reified T> ModuleBuilder.get(named: String? = null): T {
    return if (named != null) {
        GlobalContext.provide(named, T::class) as T
    } else {
        GlobalContext.provide(T::class) as T
    }
}

inline fun <reified T : Any> ModuleBuilder.factory(crossinline block: () -> T) {
    if (exists(T::class)) {
        return
    }
    val provider =
        object : Provider<T> {
            override fun get(): T = block()
        }
    providers[NamedKClass(T::class)] = provider
}

inline fun <reified T : Any> ModuleBuilder.factory(
    named: String,
    crossinline block: () -> T,
) {
    if (exists(named, T::class)) {
        return
    }
    val provider =
        object : Provider<T> {
            override fun get(): T = block()
        }
    providers[NamedKClass(named, T::class)] = provider
}

inline fun <reified T : Any> ModuleBuilder.single(crossinline block: () -> T) {
    if (exists(T::class)) {
        return
    }
    val lazyProvider = LazyProvider { block() }
    providers[NamedKClass(T::class)] = lazyProvider
}

inline fun <reified T : Any> ModuleBuilder.single(
    named: String,
    crossinline block: () -> T,
) {
    if (exists(named, T::class)) {
        return
    }
    val lazyProvider = LazyProvider { block() }
    providers[NamedKClass(named, T::class)] = lazyProvider
}

inline fun <reified VM : ViewModel> ModuleBuilder.viewModel(crossinline block: () -> VM) {
    if (exists(VM::class)) {
        return
    }
    providers[NamedKClass(VM::class)] = LazyProvider { ViewModelFactory(VM::class) { block() } }
}

inline fun <reified VM : ViewModel> AppCompatActivity.viewModel(): Lazy<VM> =
    lazy {
        val factory = GlobalContext.provideViewModelFactory(VM::class)
        val instance = ViewModelProvider(this, factory)[VM::class.java]
        GlobalContext.inject(instance, VM::class)
        instance
    }
