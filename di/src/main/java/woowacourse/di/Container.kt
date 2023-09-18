package woowacourse.di

import woowacourse.di.annotation.InjectField
import woowacourse.di.annotation.Singleton
import kotlin.IllegalStateException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation

class Container(private val module: Module) {

    private val instances = mutableMapOf<KClass<*>, Any?>()

    init {
        createSingleTon()
    }

    private fun createSingleTon() {
        module::class.declaredMemberFunctions.forEach { provider ->
            if (provider.hasAnnotation<Singleton>()) {
                val instance =
                    createInstance(provider) ?: throw IllegalStateException("잘못된 인스턴스 값 입니다")
                instances[instance::class] = instance
            }
        }
    }

    fun getInstance(clazz: KClass<*>): Any? {
        if (instances[clazz] != null) return instances[clazz]
        val a = module::class.declaredMemberFunctions.filter {
            clazz.supertypes.any { type -> type == it.returnType }
        }.firstOrNull() ?: return null
        return createInstance(a)
    }

    private fun createInstance(provider: KFunction<*>): Any? {
        val injectParams = provider.parameters.filter { it.hasAnnotation<InjectField>() }

        return if (injectParams.isNotEmpty()) {
            val instance = mutableListOf<Any?>()
            injectParams.forEach { param ->
                val func = module::class.declaredMemberFunctions.firstOrNull {
                    it.returnType == param.type
                } ?: throw IllegalStateException()
                instance.add(createInstance(func))
            }
            createParameterInstance(instance, provider)
        } else {
            provider.call(module)
        }
    }

    private fun createParameterInstance(
        injectParams: List<Any?>,
        provider: KFunction<*>,
    ): Any? {
        val args: MutableList<Any?> = mutableListOf(module)
        args.addAll(injectParams)

        return provider.call(*args.toTypedArray())
    }
}
