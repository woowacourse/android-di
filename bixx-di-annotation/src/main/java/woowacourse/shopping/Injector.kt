package woowacourse.shopping

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

@Suppress("UNCHECKED_CAST")
object Injector {
    fun <T> inject(target: KClass<*>, nameTag: String? = null): T {
        return (DependencyContainer.getInstance(target, nameTag) ?: createInstance(target)) as T
    }

    fun <T> injectSingleton(target: KClass<*>, nameTag: String? = null): T {
        return DependencyContainer.getInstance(target, nameTag) as T
    }

    private fun createSingletonInstance(target: KClass<*>): Any {
        val instance = createInstance(target)
        DependencyContainer.addInstance(target, instance)
        return instance
    }

    private fun createInstance(target: KClass<*>): Any {
        val constructor =
            requireNotNull(target.primaryConstructor) { "{${target.simpleName}}의 주 생성자를 찾을 수 없습니다" }
        val paramInstances = provideConstructorParameters(constructor)

        val instance = constructor.call(*paramInstances.toTypedArray())
        instance.injectProperties()
        return instance
    }

    private fun provideConstructorParameters(constructor: KFunction<*>): List<Any> {
        val constructorParams: List<KParameter> = constructor.parameters
        val paramInstances = mutableListOf<Any>()
        constructorParams.forEach { param: KParameter ->
            val qualifier = param.findAnnotation<Qualifier>()
            paramInstances.add(injectSingleton(param.type.jvmErasure, qualifier?.className))
        }
        return paramInstances
    }

    private fun Any.injectProperties() {
        val properties = this@injectProperties::class.declaredMemberProperties
        properties.forEach {
            it.findAnnotation<Inject>() ?: return@forEach
            this@injectProperties::class.java.getDeclaredField(it.name).apply {
                isAccessible = true
                set(this@injectProperties, injectSingleton(it.returnType.jvmErasure))
            }
        }
    }
}
