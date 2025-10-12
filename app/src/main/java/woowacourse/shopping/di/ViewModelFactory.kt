package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

class ViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass: KClass<T> = modelClass.kotlin
        val constructor: KFunction<T> =
            kClass.constructors.firstOrNull { constructor: KFunction<T> ->
                constructor.parameters.all { param: KParameter ->
                    val paramClass = param.type.classifier as? KClass<*>
                    paramClass != null && appContainer.canResolve(paramClass)
                }
            }
                ?: throw IllegalArgumentException("${kClass.simpleName}에 주입 가능한 생성자가 없습니다.")

        val args: Array<Any> =
            constructor.parameters
                .map { param: KParameter ->
                    val paramClass: KClass<*> =
                        param.type.classifier as? KClass<*>
                            ?: throw IllegalArgumentException("${param}의 타입을 확인할 수 없습니다.")
                    appContainer.get(paramClass)
                }.toTypedArray()

        return constructor.call(*args)
    }
}
