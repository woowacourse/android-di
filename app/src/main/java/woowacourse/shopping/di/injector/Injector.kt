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
        val constructor = modelClass.primaryConstructor ?: throw IllegalArgumentException()
        val parameters = constructor.parameters

        val arguments = getArguments(parameters)
        return constructor.callBy(arguments)
    }

    private fun getArguments(parameters: List<KParameter>): Map<KParameter, Any?> {
        return parameters.associateWith { parameter ->
            container.getInstance(parameter.type.jvmErasure)
                ?: throw IllegalArgumentException()
        }
    }
}
