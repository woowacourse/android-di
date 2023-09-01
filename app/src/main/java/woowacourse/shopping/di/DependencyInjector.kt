package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

inline fun <reified T : Any> inject(): T {
    val primaryConstructor = T::class.primaryConstructor
        ?: throw Throwable("주 생성자를 찾을 수 없습니다.")

    return primaryConstructor.call(*primaryConstructor.parameters.instantiate())
}

fun List<KParameter>.instantiate(): Array<Any?> =
    map { (it.type.classifier as KClass<*>).instantiate() }.toTypedArray()

fun KClass<*>.instantiate(): Any? =
    if (companionObject?.isSubclassOf(SingletonObject::class) == true) {
        (companionObjectInstance as SingletonObject<*>).get()
    } else {
        primaryConstructor?.call()
    }

inline fun <reified T : ViewModel> getInjectedViewModelFactory(): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            inject<T>()
        }
    }
