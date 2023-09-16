package woowacourse.di

import woowacourse.di.annotation.InjectField
import java.lang.IllegalStateException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf

class Container(private val module: Module) {

    private val instances = mutableMapOf<KClass<*>, Any?>()

    init {
        runModule()
    }

    private fun runModule() {
        module::class.declaredMemberFunctions.forEach { provider ->
            createInstance(provider)
        }
    }

    private fun createInstance(provider: KFunction<*>) {
        val injectParams = provider.parameters.filter { it.hasAnnotation<InjectField>() }

        if (injectParams.isNotEmpty()) {
            injectParams.forEach { param ->
                val func = module::class.declaredMemberFunctions.firstOrNull {
                    it.returnType == param.type
                } ?: throw java.lang.IllegalStateException()
                createInstance(func)
            }
            createParameterInstance(injectParams, provider)
        } else {
            provider.call(module)?.let {
                instances[it::class] = it
            }
        }
    }

    private fun createParameterInstance(
        injectParams: List<KParameter>,
        provider: KFunction<*>,
    ) {
        val args: MutableList<Any?> = mutableListOf(module)
        injectParams.forEach { param ->
            val parameterInstance = instances.filter { instance ->
                instance.key.supertypes.any { param.type == it }
            }.map { it.value }.firstOrNull()
                ?: throw IllegalStateException("일치하는 값이 없습니다")
            args.add(parameterInstance)
        }

        provider.call(*args.toTypedArray())?.let {
            instances[it::class] = it
        }
    }

    fun getInstance(type: KClass<*>): Any? {
        if (instances[type] != null) return instances[type]
        val key = instances.keys.firstOrNull { it.isSubclassOf(type) }
        return instances[key]
    }
}
