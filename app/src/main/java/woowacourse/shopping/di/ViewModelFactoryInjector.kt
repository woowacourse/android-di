package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object ViewModelFactoryInjector : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass: KClass<T> = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalStateException(ERROR_NO_CONSTRUCTOR.format(kClass::simpleName))

        val params =
            constructor.parameters.map { param ->
                RepositoryProvider.get(param.type.classifier as KClass<*>)
            }.toTypedArray()
        return constructor.call(*params) as T
    }

    private const val ERROR_NO_CONSTRUCTOR = "%s 클래스에 기본 생성자가 존재하지 않습니다."
}
