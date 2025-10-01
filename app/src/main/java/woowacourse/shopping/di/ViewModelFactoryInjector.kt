package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object ViewModelFactoryInjector {
    private const val ERROR_NO_CONSTRUCTOR = "%s 클래스에 기본 생성자가 존재하지 않습니다."

    fun <T : ViewModel> create(viewModelClass: KClass<T>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
                val constructor =
                    viewModelClass.primaryConstructor
                        ?: throw IllegalStateException(ERROR_NO_CONSTRUCTOR.format(viewModelClass::simpleName))
                val params =
                    constructor.parameters.map { param ->
                        RepositoryProvider.get(param.type.classifier as KClass<*>)
                    }.toTypedArray()

                @Suppress("UNCHECKED_CAST")
                return constructor.call(*params) as VM
            }
        }
    }
}
