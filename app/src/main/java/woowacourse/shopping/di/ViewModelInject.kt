package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

inline fun <reified T : ViewModel> viewModelInject(): ViewModelProvider.Factory {
    val repository = T::class
        .primaryConstructor
        ?.parameters
        ?: throw NullPointerException("주 생성자가 없습니다.")

    val instances = repository.map {
        val type: KClass<*> = it.type.jvmErasure
        RepositoryContainer.getInstance(type)
    }

    return viewModelFactory {
        initializer {
            T::class.primaryConstructor!!.call(instances)
        }
    }
}
