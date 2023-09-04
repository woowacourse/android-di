package woowacourse.shopping.di

import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
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
        if (!::dependencies.isInitialized)
            throw IllegalStateException("의존이 초기화되지 않았습니다.")
        dependencies::class.declaredMemberProperties.forEach {
            if (type.isSupertypeOf(it.returnType)) {
                return it.getter.call(dependencies)
            }
        }
        return null
    }

    private fun instantiate(type: KType): Any {
        val constructor = type.jvmErasure.primaryConstructor
            ?: throw IllegalArgumentException("$type 클래스의 주 생성자가 존재하지 않습니다.")
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
