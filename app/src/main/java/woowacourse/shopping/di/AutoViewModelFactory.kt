package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object AutoViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: error("주 생성자를 찾을 수 없습니다: ${modelClass.name}")

        val dependencies =
            constructor.parameters.map { parameter ->
                val dependencyType =
                    parameter.type.classifier as? KClass<*>
                        ?: error("의존성 타입을 찾을 수 없습니다: ${parameter.name}")

                DependencyContainer.get(dependencyType)
            }

        return constructor.call(*dependencies.toTypedArray())
    }
}
