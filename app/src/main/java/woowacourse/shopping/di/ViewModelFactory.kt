package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

class ViewModelFactory(
    private val dependencies: Map<KClass<*>, Any>,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass: KClass<T> = modelClass.kotlin
        val constructor: KFunction<T> =
            kClass.constructors.firstOrNull { constructor: KFunction<T> ->
                constructor.parameters.all { param: KParameter ->
                    dependencies.containsKey(param.type.classifier)
                }
            } ?: throw IllegalArgumentException(ERROR_CONSTRUCTOR_NOT_FOUND)

        val args: Array<Any> =
            constructor.parameters
                .map { param: KParameter ->
                    dependencies[param.type.classifier]
                        ?: throw IllegalArgumentException(ERROR_DEPENDENCY_NOT_FOUND)
                }.toTypedArray()

        return constructor.call(*args)
    }

    companion object {
        private const val ERROR_CONSTRUCTOR_NOT_FOUND = "주입 가능한 생성자를 찾을 수 없습니다."
        private const val ERROR_DEPENDENCY_NOT_FOUND = "주입 가능한 의존성을 찾을 수 없습니다."
    }
}
