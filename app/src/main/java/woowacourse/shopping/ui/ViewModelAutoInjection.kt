package woowacourse.shopping.ui

import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.woowacourse.di.DiActivity
import com.woowacourse.di.annotations.injectFields
import woowacourse.shopping.shoppingapp.di.ViewModelLifecycleModule

@MainThread
inline fun <reified VM : ViewModel> DiActivity.viewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = {
        inject(this)
    },
): Lazy<VM> {
    return this.viewModels(
        extrasProducer = extrasProducer,
        factoryProducer = factoryProducer,
    )
}

fun inject(diActivity: DiActivity): ViewModelProvider.Factory = ViewModelComponent(diActivity)

class ViewModelComponent(
    private val diActivity: DiActivity,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val diInjector = diActivity.diInjector
        diActivity.diInjector.addModule(ViewModelLifecycleModule())

        val constructor =
            modelClass.constructors.firstOrNull()
                ?: throw IllegalArgumentException()

        val parameters =
            constructor.parameters.map { parameter ->
                diInjector.diContainer.match(parameter.type.kotlin)
            }.toTypedArray()

        val viewModel = constructor.newInstance(*parameters) as T
        injectFields(diInjector.diContainer, viewModel)

        return viewModel
    }
}
