package woowacourse.shopping.di

import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

object DependencyInjector {
    lateinit var dependencies: Dependencies

    inline fun <reified T> inject(): T {
        return inject(typeOf<T>()) as T
    }

    fun inject(type: KType): Any {
        return findSingleton(type) ?: instantiate(type)
    }

    private fun findSingleton(type: KType): Any? {
        dependencies::class.declaredMemberProperties.forEach {
            if (type.isSupertypeOf(it.returnType)) {
                return it.getter.call(dependencies)
            }
        }
        return null
    }

    private fun instantiate(type: KType): Any {
        val constructor = type.jvmErasure.primaryConstructor ?: throw IllegalStateException()
        val parameters = constructor.parameters
        val arguments = gatherArguments(parameters)
        return constructor.call(*arguments)
    }

    private fun gatherArguments(parameters: List<KParameter>): Array<Any> {
        return parameters.map { parameter ->
            inject(parameter.type)
        }.toTypedArray()
    }
}
