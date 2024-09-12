package woowacourse.shopping.di

import kotlin.reflect.KClass

object DiContainer {
    private val modules = mutableListOf<Module>()
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun initModule(vararg module: Module) {
        modules.addAll(module)
        module.forEach { it.provideInstance(this) }
    }

    fun addInstance(
        classType: KClass<*>,
        instance: Any,
    ) {
        instances[classType] = instance
    }

    fun getInstanceOrNull(classType: KClass<*>): Any? {
        return instances[classType]
    }
}
