package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return Injector.inject(modelClass.kotlin)
    }
}

// inline fun <reified T : ViewModel> viewModelInject(): ViewModelProvider.Factory {
//    val primaryConstructor = T::class.primaryConstructor ?: throw NullPointerException("주 생성자가 없습니다.")
//
//    val repository = primaryConstructor.parameters
//
//    val instances = repository.map {
//        val type: KClass<*> = it.type.jvmErasure
//        Container.getInstance(type)
//    }
//
//    return viewModelFactory {
//        initializer {
//            primaryConstructor.call(*instances.toTypedArray())
//        }
//    }
// }
