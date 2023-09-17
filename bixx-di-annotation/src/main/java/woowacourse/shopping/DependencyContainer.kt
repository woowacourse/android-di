package woowacourse.shopping

import kotlin.reflect.KClass

object DependencyContainer {
    private val container = mutableMapOf<KClass<*>, MutableList<Any>>()
    fun addInstance(clazz: KClass<*>, instance: Any) {
        if (container[clazz] == null) {
            container[clazz] = mutableListOf(instance)
            return
        }
        container[clazz]!!.add(instance)
    }

    fun getInstance(clazz: KClass<*>, nameTag: String? = null): Any? {
        if (nameTag != null) {
            return container[clazz]?.find { it::class.simpleName == nameTag }
        }
        return container[clazz]?.first()
    }
}
