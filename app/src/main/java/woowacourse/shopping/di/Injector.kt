package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.primaryConstructor

class Injector(private val modules: List<Module>) {

    private val providers: MutableMap<String, Any?> =
        mutableMapOf<String, Any?>().apply {
            modules.forEach { module ->
                module::class.declaredMemberFunctions.forEach {
                    val returnType = it.returnType.toString()
                    val instance = it.call(module)
                    this[returnType] = instance
                }
            }
        }

    fun <T : Any> inject(clazz: KClass<T>): T {
        val primaryConstructor =
            clazz.primaryConstructor ?: throw IllegalArgumentException("주생성자 없음")

        val args = getArguments(primaryConstructor.parameters)

        return primaryConstructor.call(*args.toTypedArray())
    }

    private fun getArguments(parameters: List<KParameter>): List<Any> {
        return parameters.map {
            val paramType = it.type.toString()
            providers[paramType] ?: throw IllegalArgumentException("의존성 주입할 인스턴스 없음: $paramType")
        }
    }
}
