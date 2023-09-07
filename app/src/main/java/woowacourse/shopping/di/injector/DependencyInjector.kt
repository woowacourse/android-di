package woowacourse.shopping.di.injector

import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.util.throwNotExistDependency
import woowacourse.shopping.di.util.validateHasPrimaryConstructor
import kotlin.reflect.KParameter
import kotlin.reflect.javaType

object DependencyInjector {
    val dependencies = mutableMapOf<Class<*>, Any>()

    inline fun <reified T : Any> inject(instance: T) {
        dependencies[T::class.java] = instance
    }

    inline fun <reified T : Any> inject(): T {
        val primaryConstructor = validateHasPrimaryConstructor<T>()
        val args = getArgs(primaryConstructor.parameters)

        val instance = primaryConstructor.call(*args.toTypedArray())
        injectFields(instance)

        return instance
    }

    inline fun <reified T : Any> injectFields(instance: T) {
        instance::class.java.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(Inject::class.java)) {
                val dependency = dependencies[field.type] ?: throwNotExistDependency(field.type)
                field.isAccessible = true
                field.set(instance, dependency)
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getArgs(parameters: List<KParameter>): List<Any> = parameters.map { param ->
        val type = param.type.javaType
        dependencies[type] ?: throwNotExistDependency(type.javaClass)
    }

    fun clear() {
        dependencies.clear()
    }
}

class ClassInjectorDsl {
    inline fun <reified T : Any> inject(instance: T) {
        DependencyInjector.inject(instance)
    }
}

fun modules(block: ClassInjectorDsl.() -> Unit) {
    ClassInjectorDsl().apply(block)
}
