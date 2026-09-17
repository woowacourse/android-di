package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ReflectionViewModelFactory(
    private val container: DependencyContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor =
            requireNotNull(modelClass.kotlin.primaryConstructor) {
                "${modelClass.name}의 주 생성자를 찾을 수 없습니다."
            }

        val dependencies =
            constructor.parameters.map { parameter ->
                val dependencyType =
                    requireNotNull(
                        parameter.type.classifier as? KClass<*>,
                    ) {
                        "${parameter.name}의 타입을 확인할 수 없습니다."
                    }

                container.resolve(dependencyType)
            }

        val viewModel =
            constructor.call(*dependencies.toTypedArray())

        return modelClass.cast(viewModel)
    }
}
