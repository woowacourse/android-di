package woowacourse.shopping.otterdi

import woowacourse.shopping.otterdi.annotation.Qualifier
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

class DependencyContainer(private val modules: List<Module>) {
    constructor(vararg modules: Module) : this(modules.toList())

    private val instances: MutableMap<String, Any?> = mutableMapOf<String, Any?>().apply {
        modules.forEach { module ->
            module::class.declaredMemberFunctions.forEach {
                val returnType = it.returnType.toString()
                val instance = it.call(module)
                if (it.findAnnotation<Qualifier>() == null) {
                    this[returnType] = instance
                } else {
                    val implementationName =
                        it.findAnnotation<Qualifier>()?.implementationName.toString()
                    this[implementationName] = instance
                }
            }
        }
    }

    fun getInstance(type: String): Any {
        return instances[type] ?: throw IllegalArgumentException("${type}에 대한 의존성을 가져오는데 실피하였습니다.")
    }

    fun getInstances(params: List<KParameter>): List<Any> {
        return params.map { param ->
            if (param.findAnnotation<Qualifier>() == null) {
                getInstance(param.type.toString())
            } else {
                getInstance(param.findAnnotation<Qualifier>()?.implementationName.toString())
            }
        }
    }
}
