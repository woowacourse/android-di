package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

inline fun <reified T : ViewModel> viewModelInject(): ViewModelProvider.Factory {
    val primaryConstructor = T::class.primaryConstructor ?: throw NullPointerException("주 생성자가 없습니다.")

    val repository = primaryConstructor.parameters

    val instances = repository.map {
        val type: KClass<*> = it.type.jvmErasure
        RepositoryContainer.getInstance(type)
    }

    return viewModelFactory {
        initializer {
            primaryConstructor.call(*instances.toTypedArray())
        }
    }
}
