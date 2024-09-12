package org.aprilgom.androiddi

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import javax.inject.Inject
import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmName

fun module(block: ModuleBuilder.() -> Unit): ModuleBuilder {
    val moduleBuilder = ModuleBuilder()
    moduleBuilder.block()
    return moduleBuilder
}

fun diContainer(block: DIContainerBuilder.() -> Unit) {
    DIContainerBuilder().apply(block).build().inject()
}

fun DIContainerBuilder.modules(vararg moduleBuilders: ModuleBuilder) {
    this.moduleBuilders = moduleBuilders.toList()
}

inline fun <reified T : Any> ModuleBuilder.factory(crossinline block: () -> T) {
    if (exists(T::class)) {
        return
    }
    val provider = object : Provider<T> {
        override fun get(): T = block()
    }
    providers[NamedKClass(T::class)] = provider
}

inline fun <reified T : Any> ModuleBuilder.factory(named: String, crossinline block: () -> T) {
    if (exists(named, T::class)) {
        return
    }
    val provider = object : Provider<T> {
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

inline fun <reified T : Any> ModuleBuilder.single(named: String, crossinline block: () -> T) {
    if (exists(named, T::class)) {
        return
    }
    val lazyProvider = LazyProvider { block() }
    providers[NamedKClass(named, T::class)] = lazyProvider
}

/*
inline fun <reified T : ViewModel> ModuleBuilder.viewModel(crossinline block: () -> T) {
    if (exists(T::class)) {
        return
    }
    val viewModelStoreOwner = context as ViewModelStoreOwner
    val vmProvider = VMProvider(viewModelStoreOwner, T::class) {
        block()
    }
    providers[NamedKClass(T::class)] = vmProvider
}

inline fun <reified VM : ViewModel> AppCompatActivity.viewModel(): Lazy<VM> {
    return lazy { (DIContainer.provide(VM::class) as VMProvider<VM>).get()}
}
*/

inline fun <reified VM : ViewModel> AppCompatActivity.viewModel(): Lazy<VM> =
    viewModels {
        viewModelFactory {
            initializer {
                val instance = VM::class.primaryConstructor?.call() ?: throw IllegalArgumentException("ViewModel ${VM::class.simpleName} must have a default constructor")
                inject(instance, VM::class)
                instance
            }
        }
    }

fun inject(instance: Any?, targetClazz: KClass<*>) {
    targetClazz.declaredMemberProperties.filter {
        it.javaField?.isAnnotationPresent(Inject::class.java) ?: false
    }.map {
        it as KMutableProperty<*>
    }.forEach {
        it.isAccessible = true
        val clazz = it.returnType.classifier as KClass<*>
        val annotationName = it.annotations.find { annotation ->
            annotation.annotationClass.hasAnnotation<Qualifier>()
        }.let { annotation ->
            annotation?.annotationClass?.simpleName
        }
        val name = annotationName ?: clazz.jvmName
        val propertyInstance = DIContainer.provide(name, it.returnType.classifier as KClass<*>)
        inject(propertyInstance, clazz)
        it.setter.call(instance, propertyInstance)
    }
}
/*
inline fun <reified VM : ViewModel> AppCompatActivity.injectViewModels(): Lazy<VM> =
    viewModels {
        viewModelFactory {
            initializer {
                DIContainer.inject<VM>()
            }
        }
    }
 */
