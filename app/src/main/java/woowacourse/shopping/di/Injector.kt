package woowacourse.shopping.di

import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.primaryConstructor

class Injector(private val repositoryModule: RepositoryModule) {

    private val repositoryMap: MutableMap<String, Any?> =
        mutableMapOf<String, Any?>().apply {
            repositoryModule::class.declaredMemberFunctions.forEach {
                val returnType = it.returnType.toString()
                val instance = it.call(repositoryModule)
                this[returnType] = instance
            }
        }

    fun <T : Any> inject(modelClass: Class<T>): T {
        val primaryConstructor = modelClass.kotlin.primaryConstructor

        return primaryConstructor?.let {
            val params = getParams(primaryConstructor.parameters)

            primaryConstructor.call(*params.toTypedArray())
        } ?: throw RuntimeException("주생성자 없음")
    }

    private fun getParams(parameters: List<KParameter>): List<Any> {
        val params = mutableListOf<Any>()

        parameters.forEach {
            val paramType = it.type.toString()
            val instance = repositoryMap[paramType]
            instance?.let { params.add(instance) }
        }

        return params
    }
}
