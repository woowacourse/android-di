package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.app.ShoppingApplication.Companion.appContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: throw IllegalArgumentException("ViewModel class must have a primary constructor: $modelClass")

        val parameters =
            constructor.parameters.map { parameter ->
                appContainer.getInstance(parameter.type.classifier as KClass<*>)
            }.toTypedArray()

        return constructor.call(*parameters)
    }
}
