package woowacourse.shopping

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class BaseViewModelFactory2(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass = modelClass.kotlin

        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("ViewModel must have a primary constructor: ${kClass.simpleName}")

        val injectedFields = kClass.memberProperties.filter { it.hasAnnotation<Inject>() }

        val constructorArgs = constructor.parameters.map { parameter ->
            appContainer.find(parameter.type.classifier as KClass<*>)
        }.toTypedArray()
        constructorArgs.forEach { arg ->
            Log.d(TAG, "constructor Arg: $arg")
        }

        val viewModel = constructor.call(*constructorArgs)
        injectedFields.forEach { field ->
            val dependency = appContainer.find(field.returnType.classifier as KClass<*>)
            field.isAccessible = true
            Log.d(TAG, "injected field: $field")
            Log.d(TAG, "dependency for field: $dependency")

            (field as KMutableProperty<*>).setter.call(viewModel, dependency)
        }

        return constructor.call(*constructorArgs)
    }
}

private const val TAG = "BaseViewModelFactory2"