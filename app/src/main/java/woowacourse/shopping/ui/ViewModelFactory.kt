package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.di.DependencyContainer

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor =
            modelClass.constructors.firstOrNull()
                ?: throw IllegalArgumentException("Unknown ViewModel")

        val params =
            constructor.parameters.map { parameter ->
                when (val parameterType = parameter.type) {
                    is Class<*> -> DependencyContainer.instance<T>(parameterType.kotlin) as Any
                    else -> throw IllegalArgumentException("Unknown parameter type: $parameterType")
                }
            }.toTypedArray()

        val viewModel = constructor.newInstance(*params) as T
        DependencyContainer.injectProperty(viewModel)
        return viewModel
    }
}
