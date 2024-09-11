package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

@Suppress("UNCHECKED_CAST")
class DiContainer(
    private val diModule: DiModule,
) {
    private val dependencies: MutableMap<KClass<*>, Any> = mutableMapOf()
    private val functions: Collection<KFunction<*>> by lazy { diModule::class.declaredFunctions.filter { it.visibility == KVisibility.PUBLIC } }

    fun <T : Any> getInstance(clazz: KClass<T>): T {
        return (dependencies[clazz] ?: addDependency(functions, clazz)) as T
    }

    private fun addDependency(functions: Collection<KFunction<*>>, clazz: KClass<*>): Any {
        val func = functions.first { it.returnType.jvmErasure == clazz }
        if (func.valueParameters.isEmpty()) {
            addInstance(clazz, func.call(diModule))
            return getInstance(clazz)
        }
        val params = func.valueParameters.map { param ->
            val paramInstance = param.type.jvmErasure
            getInstance(paramInstance)
        }
        addInstance(clazz, func.call(diModule, *params.toTypedArray()))
        return getInstance(clazz)
    }

    private fun addInstance(
        classType: KClass<*>,
        instance: Any?,
    ) {
        instance?.let {
            dependencies[classType] = it
        }
    }
}
