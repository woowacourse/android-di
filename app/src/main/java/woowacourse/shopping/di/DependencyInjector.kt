package woowacourse.shopping.di

import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

object DependencyInjector {
    inline fun <reified T> inject(): T {
        val constructor = T::class.constructors
        return findSingleton(typeOf<T>()) as T ?: instantiate(constructor.first()) as T
    }

    fun instantiate(constructor: KFunction<*>): Any {
        val parameters = constructor.parameters
        return constructor.call(
            *(gatherArguments(parameters))
        ) ?: throw IllegalStateException()
    }

    fun findSingleton(type: KType): Any? {
        Singleton::class.declaredMemberProperties.forEach {
            if (type.isSubtypeOf(it.returnType)) return it.get(Singleton) ?: return@forEach
        }
        return null
    }

    private fun gatherArguments(parameters: List<KParameter>): Array<Any> {
        return parameters.map { parameter ->
            inject(parameter.type)
        }.toTypedArray()
    }

    private fun inject(type: KType): Any {
        val constructor = type::class.constructors
        return findSingleton(type) ?: instantiate(constructor.first())
    }
}
