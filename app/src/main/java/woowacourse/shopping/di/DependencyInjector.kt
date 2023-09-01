package woowacourse.shopping.di

import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubtypeOf

object DependencyInjector {
    inline fun <reified T> inject(): T {
        val constructor = T::class.constructors.first()
        return instantiate(constructor) as T
    }

    fun instantiate(constructor: KFunction<*>): Any {
        val parameters = constructor.parameters
        return constructor.call(
            *(gatherArguments(parameters))
        ) ?: throw IllegalStateException()
    }

    private fun gatherArguments(parameters: List<KParameter>): Array<Any> {
        return parameters.map { parameter ->
            findSingleton(parameter) ?: inject(parameter.type)
        }.toTypedArray()
    }

    private fun findSingleton(parameter: KParameter): Any? {
        Singleton::class.declaredMemberProperties.forEach {
            if (parameter.type.isSubtypeOf(it.returnType)) return it.get(Singleton)
                ?: return@forEach
        }
        return null
    }

    private fun inject(type: KType): Any {
        val constructor = type::class.constructors.first()
        return instantiate(constructor)
    }
}
