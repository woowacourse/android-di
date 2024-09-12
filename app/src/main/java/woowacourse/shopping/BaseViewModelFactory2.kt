package woowacourse.shopping

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.Inject
import woowacourse.shopping.di.Qualifier
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

        val constructorArgs =
            constructor.parameters.map { parameter ->
                appContainer.find(parameter.type.classifier as KClass<*>)
            }.toTypedArray()

        val viewModel = constructor.call(*constructorArgs)

        injectedFields.forEach { field ->
            field.isAccessible = true // private 필드에 접근할 수 있도록 설정
            val allAnnotation = field.annotations.also { Log.d(TAG, "allAnnotation: $it") }
            val allQualifiers = allAnnotation.filterIsInstance<Qualifier>().also { Log.d(TAG, "allQualifiers: $it") }

//            val allAnnotationHasQualifier = allAnnotation.find {
//                it.annotationClass.hasAnnotation<Qualifier>()
//            }.also { Log.d(TAG, "allAnnotationHasQualifier: $it") }

            // 필드에 붙은 Qualifier 찾기
            val qualifier = field.annotations.filterIsInstance<Qualifier>().firstOrNull()
            Log.d(TAG, "field: $field, qualifier: $qualifier")

            val dependency =
                if (qualifier != null) {
                    appContainer.find(field.returnType.classifier as KClass<*>, qualifier)
                } else {
                    appContainer.find(field.returnType.classifier as KClass<*>)
                } ?: throw IllegalArgumentException("Unresolved dependency for field: ${field.name}")

            if (field is KMutableProperty<*>) {
                field.setter.call(viewModel, dependency)
            } else {
                throw IllegalArgumentException("Field ${field.name} is not mutable and cannot be injected")
            }
        }

        return viewModel
    }
}

private const val TAG = "BaseViewModelFactory2"
