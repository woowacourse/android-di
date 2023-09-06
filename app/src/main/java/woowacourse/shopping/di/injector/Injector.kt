package woowacourse.shopping.di.injector

import woowacourse.shopping.di.module.Module
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.primaryConstructor

class Injector(private val modules: List<Module>) {

    constructor(vararg modules: Module) : this(modules.toList())

    private val providers: MutableMap<KType, Any?> =
        mutableMapOf<KType, Any?>().apply {
            modules.forEach { module ->
                module::class.declaredMemberFunctions.forEach {
                    val returnType = it.returnType
                    val instance = it.call(module)
                    this[returnType] = instance
                }
            }
        }

    inline fun <reified T : Any> inject(): T {
        val primaryConstructor = T::class.primaryConstructor
            ?: throw NullPointerException("${T::class.simpleName} 클래스의 주생성자를 가져오는데 실패하였습니다.")
        val params = getParams(primaryConstructor.parameters)
        return primaryConstructor.call(*params.toTypedArray())
    }

    fun getParams(parameters: List<KParameter>): List<Any> {
        val params = mutableListOf<Any>()

        parameters.forEach {
            val paramType = it.type
            val instance = providers[paramType]
            instance?.let { params.add(instance) }
        }

        return params
    }
}
