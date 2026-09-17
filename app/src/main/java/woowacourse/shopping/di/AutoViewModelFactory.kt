package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class AutoViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val classReflection = modelClass.kotlin
        val constructor = classReflection.primaryConstructor ?: throw IllegalArgumentException()
        val parameters = constructor.parameters
        val args =
            parameters.map { parameter ->
                val dependencyClass =
                    parameter.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException()

                DependencyContainer.getInstance(dependencyClass)
            }
        return constructor.call(*args.toTypedArray())
    }
}
