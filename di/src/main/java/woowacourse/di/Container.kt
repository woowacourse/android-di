package woowacourse.di

import woowacourse.di.SingletonContainer.instances
import woowacourse.di.annotation.InjectField
import woowacourse.di.annotation.Singleton
import woowacourse.di.module.Module
import kotlin.IllegalStateException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation

class Container(private val module: Module) {

    init {
        createSingleton()
    }

    private fun createSingleton() {
        module::class.declaredMemberFunctions.forEach { provider ->
            if (provider.hasAnnotation<Singleton>()) {
                val instance =
                    createInstance(provider)
                        ?: throw IllegalStateException("${provider.returnType} 인스턴스 생성에 실패했습니다")
                instances[instance::class] = instance
            }
        }
    }

    fun getInstance(clazz: KClass<*>): Any? {
        if (instances[clazz] != null) return instances[clazz]
        val function = module::class.declaredMemberFunctions.firstOrNull {
            clazz.createType() == it.returnType
        } ?: return null
        return createInstance(function)
    }

    private fun createInstance(provider: KFunction<*>): Any? {
        val injectParams = provider.parameters.filter { it.hasAnnotation<InjectField>() }

        return if (injectParams.isNotEmpty()) {
            val instance = mutableListOf<Any?>()
            injectParams.forEach { param ->
                val func = module::class.declaredMemberFunctions.firstOrNull {
                    it.returnType == param.type
                } ?: throw IllegalStateException("${param.type}을 생성하는 함수가 없습니다")
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
