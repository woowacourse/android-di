package woowacourse.shopping.ui.util

import kotlin.reflect.KClass
import kotlin.reflect.KClassifier

class DefaultDependencyContainer : DependencyContainer {
    private val dependencies: MutableMap<KClassifier, KClass<*>> =
        mutableMapOf()

    private val cachedInstances: MutableMap<KClassifier, Any> =
        mutableMapOf()

    override fun <T : Any> getInstance(kClassifier: KClassifier): T? {
        return cachedInstances[kClassifier] as T?
    }

    override fun <T : Any> getImplement(kClassifier: KClassifier): KClass<T>? {
        return dependencies[kClassifier] as KClass<T>?
    }

    override fun <T : Any> setDependency(kClassifier: KClassifier, kClass: KClass<T>) {
        dependencies[kClassifier] = kClass
    }

    override fun setInstance(kClassifier: KClassifier, instance: Any) {
        if (dependencies.contains(kClassifier)) {
            cachedInstances[kClassifier] = instance
        }
    }
}
