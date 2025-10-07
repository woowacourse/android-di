package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("No primary constructor for ${modelClass.simpleName}")

        val params =
            constructor.parameters.associateWith { param ->
                Dependency.get(param.type.classifier as KClass<*>)
            }

        return constructor.callBy(params)
    }
}
