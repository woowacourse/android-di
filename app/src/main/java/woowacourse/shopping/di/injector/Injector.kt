package woowacourse.shopping.di.injector

import woowacourse.shopping.di.container.ShoppingContainer
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class Injector(
    private val container: ShoppingContainer,
) {
    fun <T : Any> create(modelClass: Class<T>): T {
        val constructor = modelClass.kotlin.primaryConstructor ?: throw IllegalArgumentException()
        val parameters = constructor.parameters

        val arguments = getArguments(parameters)

        return constructor.callBy(arguments)
    }

    private fun getArguments(parameters: List<KParameter>): Map<KParameter, Any?> {
        val properties = container::class.declaredMemberProperties

        return parameters.associateWith { parameter ->
            val property = properties.find { it.returnType == parameter.type }
                ?: throw IllegalArgumentException()
            property.getter.call(container)
        }
    }
}
