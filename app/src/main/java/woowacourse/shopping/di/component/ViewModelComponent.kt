package woowacourse.shopping.di.component

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.annotation.DiViewModel
import woowacourse.shopping.di.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

fun inject(): ViewModelProvider.Factory = ViewModelComponent()

private class ViewModelComponent : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val clazz = modelClass.kotlin
        if (!clazz.hasAnnotation<DiViewModel>()) {
            throw IllegalArgumentException(ERROR_ANNOTATION)
        }
        val constructor =
            modelClass.constructors.firstOrNull()
                ?: throw IllegalArgumentException(ERROR_CONSTRUCTOR.format(modelClass))

        val parameters =
            constructor.parameterTypes.map { parameterType ->
                DiSingletonComponent.match(parameterType.kotlin)
            }.toTypedArray()

        val viewModel = constructor.newInstance(*parameters) as T
        injectFields(viewModel)
        return viewModel
    }

    private fun <T : Any> injectFields(instance: T) {
        val clazz = instance::class.java
        clazz.declaredFields.forEach { field ->
            if(field.isAnnotationPresent(Inject::class.java)){
                field.isAccessible = true
                val fieldInstance = DiSingletonComponent.match(field.type.kotlin)
                field.set(instance,fieldInstance)
            }
        }
    }

    companion object {
        private const val ERROR_CONSTRUCTOR = "No suitable constructor for %s"
        private const val  ERROR_ANNOTATION =" ViewModel class must be annotated with @DiViewModel"
    }
}
