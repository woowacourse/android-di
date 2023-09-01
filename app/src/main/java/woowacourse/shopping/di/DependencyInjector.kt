package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

inline fun <reified T : Any> inject(): T {
    val arguments = T::class
        .primaryConstructor
        ?.parameters
        ?.map { it.type.classifier as KClass<*> }
        ?.map { it.primaryConstructor?.call() }
        ?.toTypedArray() as Array<out Any?>

    return T::class.primaryConstructor?.call(*arguments)
        ?: throw Throwable("주 생성자를 찾을 수 없습니다.")
}

inline fun <reified T : ViewModel> getViewModelFactory(): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            inject<T>()
        }
    }
