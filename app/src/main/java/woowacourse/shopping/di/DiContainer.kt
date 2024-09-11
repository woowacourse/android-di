package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.jvmErasure

@Suppress("UNCHECKED_CAST")
class DiContainer(private val diModule: DiModule) {
    private val dependencies: MutableMap<KClass<*>, Any> = mutableMapOf()

    init {
        addDependencies(diModule::class.declaredFunctions.filter { it.visibility == KVisibility.PUBLIC })
    }

    fun <T : Any> getInstance(clazz: KClass<T>): T {
        return dependencies[clazz] as? T
            ?: throw IllegalArgumentException("No instance found for class ${clazz.simpleName}")
    }

    private fun addDependencies(functions: Collection<KFunction<*>>) {
        functions.forEach { func ->
            val clazz = func.returnType.jvmErasure
            val instance = func.call(diModule)
            addInstance(clazz, instance)
        }
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