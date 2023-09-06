package woowacourse.shopping.di

import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class Injector(private val container: Container) {

    inline fun <reified T : Any> getInstance(): T {
        val constructor = T::class.primaryConstructor ?: throw IllegalStateException()
        val parameters = constructor.parameters
        val arguments = parameters.map { getArgument(it) }
        return constructor.call(*arguments.toTypedArray())
    }

    fun getArgument(param: KParameter): Any {
        container::class.declaredMemberProperties.forEach {
            if (param.type == it.returnType) {
                return it.getter.call(container) ?: throw java.lang.IllegalArgumentException()
            }
        }
        throw IllegalArgumentException()
    }
}
