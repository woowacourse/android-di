package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.di.Container
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: throw IllegalArgumentException("Unknown ViewModel")

        val params =
            constructor.parameters.map { parameter ->
                Container.instance<T>(parameter.type.classifier as KClass<*>)
            }.toTypedArray()

        return constructor.call(*params)
    }
}
