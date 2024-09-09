package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.AppContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class BaseViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("ViewModel must have a primary constructor: ${kClass.simpleName}")

        val args =
            constructor.parameters.map { parameter ->
                appContainer.find(parameter.type.classifier as KClass<*>)
            }.toTypedArray()

        return constructor.call(*args)
    }
}
