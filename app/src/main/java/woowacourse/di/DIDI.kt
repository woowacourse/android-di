package woowacourse.di

import android.util.Log
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

class InjectedContainer(
    val singletonContainer: InjectedSingletonContainer = InjectedSingletonContainer,
    val activityContainer: InjectedActivityContainer = InjectedActivityContainer,
) {
    fun add(component: InjectedComponent) {
        when (component) {
            is InjectedComponent.InjectedSingletonComponent -> singletonContainer.add(component)
            is InjectedComponent.InjectedActivityComponent -> activityContainer.add(component)
        }
    }

    fun find(clazz: KClass<*>): Any? = singletonContainer.find(clazz) ?: activityContainer.find(clazz)

    fun clearActivityScopedObjects() {
        activityContainer.clear() // Activity 소멸 시점에서 Activity scoped 객체들 제거
    }
}

object InjectedSingletonContainer {
    val components: MutableList<InjectedComponent.InjectedSingletonComponent> = mutableListOf()

    fun add(component: InjectedComponent.InjectedSingletonComponent) {
        components.add(component)
    }

    fun find(clazz: KClass<*>): Any? = components.find {
        clazz.isSuperclassOf(it.injectedClass)
    }?.instance
}

object InjectedActivityContainer {
    val components: MutableList<InjectedComponent.InjectedActivityComponent> = mutableListOf()

    fun add(component: InjectedComponent.InjectedActivityComponent) {
        components.add(component)
    }

    fun find(clazz: KClass<*>): Any? = components.find {
        clazz.isSuperclassOf(it.injectedClass)
    }?.instance.also {
        Log.d(TAG, "find: $it")
    }

    fun clear() {
        components.clear()  // Activity 소멸 시점에서 Activity-scoped 객체들 제거
    }
}

sealed class InjectedComponent {
    abstract val injectedClass: KClass<*>
    abstract val instance: Any?

    class InjectedSingletonComponent(
        override val injectedClass: KClass<*>,
        override val instance: Any,
    ) : InjectedComponent()

    class InjectedActivityComponent(
        override val injectedClass: KClass<*>,
        override val instance: Any,
        val activityClass: KClass<*>,
    ) : InjectedComponent()

}

private const val TAG = "DIDI"