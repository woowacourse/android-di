package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.SingletonObject
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

inline fun <reified T : Any> inject(): T {
    val primaryConstructor = T::class.primaryConstructor
        ?: throw Throwable("주 생성자를 찾을 수 없습니다.")

    val arguments = primaryConstructor
        .parameters
        .map { it.type.classifier as KClass<*> }
        .map {
            if (it.companionObject?.isSubclassOf(SingletonObject::class) == true) {
                (it.companionObjectInstance as SingletonObject<*>).get()
            } else {
                it.primaryConstructor?.call()
            }
        }
        .toTypedArray()

    return primaryConstructor.call(*arguments)
}

inline fun <reified T : ViewModel> getViewModelFactory(): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            inject<T>()
        }
    }
