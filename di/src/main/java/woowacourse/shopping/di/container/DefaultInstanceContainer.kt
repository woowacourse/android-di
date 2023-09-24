package woowacourse.shopping.di.container

import android.util.Log
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

class DefaultInstanceContainer(
    instances: List<Any>,
) : InstanceContainer {
    private val _instances: MutableList<Any> = instances.toMutableList()

    override val value: List<Any>
        get() = _instances.toList()

    override fun add(instance: Any) {
        Log.d("123123", "instance added: $instance")
        _instances.add(instance)
    }

    override fun find(clazz: Any): Any? {
        Log.d("123123", "find : $clazz")

        return value.find { it::class.isSubclassOf(clazz as KClass<out Any>) }
    }

    override fun remove(clazz: KClass<*>) {
        _instances.removeIf { clazz.isSubclassOf(it::class) or (it::class == clazz) }
    }

    override fun clear() {
        _instances.clear()
    }
}
