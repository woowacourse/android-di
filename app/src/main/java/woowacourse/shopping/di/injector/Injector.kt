package woowacourse.shopping.di.injector

import woowacourse.shopping.di.container.ShoppingContainer
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class Injector(
    private val container: ShoppingContainer,
) {
    fun <T : Any> create(modelClass: KClass<T>): T {
        val constructor = modelClass.primaryConstructor
            ?: throw IllegalArgumentException(ERROR_NO_CONSTRUCTOR)
        val parameters = constructor.parameters

        val arguments = getArguments(parameters)
        return constructor.callBy(arguments)
    }

    private fun getArguments(parameters: List<KParameter>): Map<KParameter, Any?> {
        return parameters.associateWith { parameter ->
            container.getInstance(parameter.type.jvmErasure)
                ?: throw IllegalArgumentException(ERROR_NO_FIELD)
        }
    }

    companion object {
        private const val ERROR_NO_CONSTRUCTOR = "주생성자가 존재하지 않습니다"
        private const val ERROR_NO_FIELD = "컨테이너에 해당 인스턴스가 존재하지 않습니다"
    }
}
