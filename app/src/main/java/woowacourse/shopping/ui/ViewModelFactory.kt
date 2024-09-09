package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.di.DependencyContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: throw IllegalArgumentException("Unknown ViewModel")

        val params = constructor.parameters.map { parameter ->
            val parameterType = parameter.type.classifier
            when (parameterType) {
                is KClass<*> -> DependencyContainer.instance<T>(parameterType)
                else -> throw IllegalArgumentException("Unknown parameter type: $parameterType")
            }
        }
        return constructor.call(*params.toTypedArray())
    }
}
