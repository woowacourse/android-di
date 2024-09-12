package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.DependencyInjector

class BaseViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    val di = DependencyInjector(appContainer)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return di.createInstance(modelClass)
//        val kClass = modelClass.kotlin
//
//        val constructor =
//            kClass.primaryConstructor
//                ?: throw IllegalArgumentException("ViewModel must have a primary constructor: ${kClass.simpleName}")
//
//        val injectedFields = kClass.memberProperties.filter { it.hasAnnotation<Inject>() }
//
//        val constructorArgs =
//            constructor.parameters.map { parameter ->
//                appContainer.find(parameter.type.classifier as KClass<*>)
//            }.toTypedArray()
//
//        val viewModel = constructor.call(*constructorArgs)
//
//        injectedFields.forEach { field ->
//            field.isAccessible = true // private 필드에 접근할 수 있도록 설정
//
//            val qualifier = field.annotations.filterIsInstance<Qualifier>().firstOrNull().also {
//                Log.d(TAG, "qualifier $it")
//            }
//
//            val dependency =
//                if (qualifier != null) {
//                    appContainer.find(field.returnType.classifier as KClass<*>, qualifier)
//                } else {
//                    appContainer.find(field.returnType.classifier as KClass<*>)
//                } ?: throw IllegalArgumentException("Unresolved dependency for field: ${field.name}")
//
//            if (field is KMutableProperty<*>) {
//                field.setter.call(viewModel, dependency)
//            } else {
//                throw IllegalArgumentException("Field ${field.name} is not mutable and cannot be injected")
//            }
//        }
//
//        return viewModel
    }
}


private const val TAG = "BaseViewModelFactory"