package woowacourse.shopping.di

import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class Injector(private val container: Container) {

    inline fun <reified T : Any> getInstance(): T {
        val constructor = T::class.primaryConstructor ?: throw IllegalStateException()
        val parameters = constructor.parameters
        return constructor.call(*getArguments(parameters).toTypedArray())
    }

    fun getArguments(parameters: List<KParameter>): MutableList<Any> {
        val arguments = mutableListOf<Any>()

        for (param in parameters) {
            arguments.add(getArgument(param))
        }
        return arguments
    }

    private fun getArgument(param: KParameter): Any {
        container::class.declaredMemberProperties.forEach {
            if (param.type == it.returnType) {
                val result = it.getter.call(container)
                if (result != null) {
                    return result
                }
            }
        }
        throw IllegalArgumentException()
    }
}
