package woowacourse.shopping

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.android.di.component.DiSingletonComponent
import com.android.di.component.DiViewModelComponent

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModelsWithAutoInject(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = { inject() },
): Lazy<VM> {
    return viewModels(
        extrasProducer = extrasProducer,
        factoryProducer = factoryProducer,
    )
}

fun inject(): ViewModelProvider.Factory = ViewModelComponent()

private class ViewModelComponent : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!DiViewModelComponent.hasAnnotation(modelClass.kotlin)) {
            throw IllegalArgumentException(ERROR_ANNOTATION)
        }
        val constructor =
            modelClass.constructors.firstOrNull()
                ?: throw IllegalArgumentException(ERROR_CONSTRUCTOR.format(modelClass))

        val parameters =
            constructor.parameters.mapIndexed { index, parameter ->
                DiSingletonComponent.match(parameter.type.kotlin)
            }.toTypedArray()

        val viewModel = constructor.newInstance(*parameters) as T
        DiViewModelComponent.injectFields(viewModel)
        return viewModel
    }

    companion object {
        private const val ERROR_CONSTRUCTOR = "No suitable constructor for %s"
        private const val ERROR_ANNOTATION = " ViewModel class must be annotated with @DiViewModel"
    }
}
