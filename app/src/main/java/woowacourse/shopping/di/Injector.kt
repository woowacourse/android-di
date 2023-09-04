package woowacourse.shopping.di

import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.primaryConstructor

class Injector(private val repositoryModule: Module) {

    private val repositoryMap: MutableMap<String, Any?> =
        mutableMapOf<String, Any?>().apply {
            repositoryModule::class.declaredMemberFunctions.forEach {
                val returnType = it.returnType.toString()
                val instance = it.call(repositoryModule)
                this[returnType] = instance
            }
        }

    inline fun <reified T : Any> inject(): T {
        val primaryConstructor = T::class.primaryConstructor ?: throw RuntimeException("주생성자 없음")

        val params = getParams(primaryConstructor.parameters)

        return primaryConstructor.call(*params.toTypedArray())
    }

    fun getParams(parameters: List<KParameter>): List<Any> {
        val params = mutableListOf<Any>()

        parameters.forEach {
            val paramType = it.type.toString()
            val instance = repositoryMap[paramType]
            instance?.let { params.add(instance) }
        }

        return params
    }
}
