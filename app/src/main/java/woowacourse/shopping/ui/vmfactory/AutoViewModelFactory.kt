package woowacourse.shopping.ui.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.ShoppingContainer
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

class AutoViewModelFactory(
    private val container: ShoppingContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel: KClass<T> = modelClass.kotlin
        val constructor =
            viewModel.primaryConstructor
                ?: viewModel.constructors.firstOrNull()
                ?: error("${viewModel.simpleName} 생성자를 찾을 수 없습니다")

        val constructorArguments: Array<Any?> =
            constructor.parameters.map { parameter: KParameter ->
                val requiredType =
                    parameter.type.classifier as? KClass<*>
                        ?: error("${parameter.type} 지원하지 않는 파라미터 입니다.")

                try {
                    container.get(requiredType)
                } catch (e: Throwable) {
                    throw IllegalStateException("의존성을 주입할 수 없습니다.", e)
                }
            }.toTypedArray()
        return constructor.call(*constructorArguments)
    }
}
