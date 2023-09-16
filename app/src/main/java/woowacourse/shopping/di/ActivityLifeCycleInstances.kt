package woowacourse.shopping.di

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.reflect.KClass

class ActivityLifeCycleInstances : DefaultLifecycleObserver {

    private var instances: MutableList<Instance<out Any>>? = mutableListOf()

    fun add(instance: Instance<*>) {
        instances?.add(instance)
    }

    fun find(clazz: KClass<*>): Any? = instances?.find { it.clazz == clazz }?.value

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        instances = mutableListOf()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        instances = null
    }
}
