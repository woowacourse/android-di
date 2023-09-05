package woowacourse.shopping.di

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

    inline fun <reified T : Any> inject(): T {
        val primaryConstructor =
            T::class.primaryConstructor ?: throw IllegalArgumentException("주생성자 없음")

        val params = getParams(primaryConstructor.parameters)

        return primaryConstructor.call(*params.toTypedArray())
    }

    fun getParams(parameters: List<KParameter>): List<Any> {
        val params = mutableListOf<Any>()

        parameters.forEach {
            val paramType = it.type.toString()
            val instance = providers[paramType] ?: throw IllegalArgumentException("의존성 주입할 인스턴스 없음")
            params.add(instance)
        }

        return params
    }
}
