package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class AutoViewModelFactory(
    private val dependencyContainer: DependencyContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: error("주 생성자를 찾을 수 없습니다: ${modelClass.name}")

        val dependencies =
            constructor.parameters.map { parameter ->
                val dependencyType =
                    parameter.type.classifier as? KClass<*>
                        ?: error("의존성 타입을 찾을 수 없습니다: ${parameter.name}")

                dependencyContainer.get(dependencyType)
            }

        val viewModel =
            modelClass.cast(constructor.call(*dependencies.toTypedArray()))
                ?: error("ViewModel 생성 결과가 요청한 타입과 다릅니다: ${modelClass.name}")
        dependencyContainer.injectMembers(viewModel)
        return viewModel
    }
}
