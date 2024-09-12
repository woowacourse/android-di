package woowacourse.shopping.di

import kotlin.reflect.KClass

object DependencyRegistry {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun initModule(module: Module) {
        module.provideDependencies(this)
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
