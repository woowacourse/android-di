package woowacourse.di

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.isSubclassOf

class Container(private val module: Module) {

    private val instances = mutableMapOf<KClass<*>, Any?>()

    init {
        createInstance()
    }

    private fun createInstance() {
        module::class.declaredMemberFunctions.forEach { provider ->
            provider.call(module)?.let {
                instances[it::class] = it
            }
        }
    }

    fun getInstance(type: KClass<*>): Any? {
        if (instances[type] != null) return instances[type]
        val key = instances.keys.firstOrNull { it.isSubclassOf(type) }
        return instances[key]
    }
}
