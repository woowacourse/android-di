package woowacourse.shopping.ui.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.firstwoosun.di.DependencyContainer

class CommonViewModelFactory(
    private val dependencyContainer: DependencyContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        val constructor = modelClass.kotlin.primaryConstructor
//            ?: error("주 생성자를 찾을 수 없음")

//        val dependencies =
//            constructor.parameters.associateWith { parameter ->
//                val parameterType = parameter.type.classifier as KClass<*>
//                dependencyContainer.getInstance(parameterType)
//            }

//        return constructor.callBy(dependencies)

        val viewModel = modelClass.getDeclaredConstructor().newInstance()
        dependencyContainer.inject(viewModel)

        return viewModel
    }
}
