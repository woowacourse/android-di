package woowacourse.shopping

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.android.di.component.DiComponent
import com.android.di_android.ActivityInjector
import woowacourse.shopping.data.di.ViewModelLifeModule

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModelsWithAutoInject(
    activityInjector: ActivityInjector,
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = {
        inject(activityInjector)
    },
): Lazy<VM> {
    return viewModels(
        extrasProducer = extrasProducer,
        factoryProducer = factoryProducer,
    )
}

fun inject(activityInjector: ActivityInjector): ViewModelProvider.Factory =
    ViewModelComponent(activityInjector)

class ViewModelComponent(
    private val activityInjector: ActivityInjector,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val diInjector = activityInjector.diInjector
        activityInjector.diInjector.addModule(ViewModelLifeModule())

        if (!DiComponent.hasViewModelAnnotation(modelClass.kotlin)) {
            throw IllegalArgumentException(ERROR_ANNOTATION)
        }

        val constructor =
            modelClass.constructors.firstOrNull()
                ?: throw IllegalArgumentException(ERROR_CONSTRUCTOR.format(modelClass))

        val parameters =
            constructor.parameters.map { parameter ->
                diInjector.diContainer.match(parameter.type.kotlin)
            }.toTypedArray()

        val viewModel = constructor.newInstance(*parameters) as T
        DiComponent.injectFields(diInjector.diContainer, viewModel)

        return viewModel
    }

    companion object {
        private const val ERROR_CONSTRUCTOR = "No suitable constructor for %s"
        private const val ERROR_ANNOTATION = " ViewModel class must be annotated with @DiViewModel"
    }
}
