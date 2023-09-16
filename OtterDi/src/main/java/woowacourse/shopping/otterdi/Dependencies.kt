package woowacourse.shopping.otterdi

import woowacourse.shopping.otterdi.annotation.Inject
import woowacourse.shopping.otterdi.annotation.Qualifier
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class Dependencies(private val modules: List<Module>) {
    constructor(vararg modules: Module) : this(modules.toList())

    private val instances: MutableMap<String, Any?> = mutableMapOf<String, Any?>().apply {
        modules.forEach { module ->
            module::class.declaredMemberFunctions.forEach {
                val key = getDependencyKey(it)
                this[key] = createInstance(it, module)
            }
        }
    }
    private val providers: MutableMap<String, KFunction<*>> =
        modules::class.declaredMemberFunctions.associateBy { it.returnType.toString() }
            .toMutableMap()

    private fun createInstance(provideFunc: KFunction<*>, receiver: Module): Any? {
        val injectParams = provideFunc.parameters.filter { it.hasAnnotation<Inject>() }
        if (injectParams.isEmpty()) return provideFunc.call(receiver)

        injectParams.forEach { param ->
            val key = getDependencyKey(param)
            if (providers[key] == null) throw IllegalArgumentException("필요한 의존성을 찾을 수 없습니다. ($key)")
            if (instances[key] == null) instances[key] = createInstance(providers[key]!!, receiver)
        }

        val args = injectParams.map { instances[it.type.toString()] }
        return provideFunc.call(receiver, args.toTypedArray())
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

    private fun getDependencyKey(function: KFunction<*>): String {
        return if (function.findAnnotation<Qualifier>() == null) {
            function.returnType.toString()
        } else {
            function.findAnnotation<Qualifier>()!!.implementationName
        }
    }

    private fun getDependencyKey(param: KParameter): String {
        return if (param.findAnnotation<Qualifier>() == null) {
            param.type.toString()
        } else {
            param.findAnnotation<Qualifier>()!!.implementationName
        }
    }
}
