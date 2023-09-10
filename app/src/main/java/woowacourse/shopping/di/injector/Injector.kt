package woowacourse.shopping.di.injector

import woowacourse.shopping.di.container.ShoppingContainer
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.jvmName

class Injector(
    private val container: ShoppingContainer,
) {
    fun <T : Any> create(clazz: KClass<T>): T {
        val instance =  container.getInstance(clazz)

        if (instance != null) return instance
        return createInstance(clazz)
    }

    private fun <T> createInstance(clazz: KClass<*>): T {
        val constructor = clazz.primaryConstructor
            ?: throw IllegalArgumentException("${clazz.jvmName} $ERROR_NO_CONSTRUCTOR")
        val parameters = constructor.parameters
        val arguments = getArguments(parameters)

        return constructor.callBy(arguments) as T
    }

        private fun getArguments(parameters: List<KParameter>): Map<KParameter, Any?> {
            return parameters.associateWith { parameter ->
                val type = parameter.type.jvmErasure
                container.getInstance(type)
                    ?: container.createInstance(type, create(type))
            }
        }

    companion object {
        private const val ERROR_NO_CONSTRUCTOR = "주생성자가 존재하지 않습니다"
        private const val ERROR_NO_FIELD = "컨테이너에 해당 인스턴스가 존재하지 않습니다"
    }
}
