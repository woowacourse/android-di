package woowacourse.shopping

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

class Creator(private val injector: Injector) {
    val dependencyContainer = injector.dependencyContainer
    fun createInstance(target: KClass<*>, qualifierTag: String? = null): Any {
        val constructor = requireNotNull(target.primaryConstructor)
        val constructorParams = constructor.getParamsInstance()
        val instance =
            constructor.call(*constructorParams.toTypedArray()).apply { injectProperties() }
        if (qualifierTag == null) {
            dependencyContainer.addInstance(target, listOf(), instance)
        } else {
            dependencyContainer.addInstance(target, listOf(Qualifier(qualifierTag)), instance)
        }
        return instance
    }

    private fun KFunction<Any>.getParamsInstance(): List<Any> {
        return parameters.map {
            val qualifier = it.annotations.filterIsInstance<Qualifier>().firstOrNull()
            injector.inject(it.type.jvmErasure, qualifier?.className)
        }
    }

    private fun Any.injectProperties() {
        this::class.declaredMemberProperties
            .asSequence()
            .filter { it.hasAnnotation<Inject>() }
            .forEach {
                val qualifier = it.annotations.filterIsInstance<Qualifier>().firstOrNull()
                it.isAccessible = true
                it.javaField?.set(
                    this,
                    injector.inject(it.returnType.jvmErasure, qualifier?.className),
                )
            }
    }
}
