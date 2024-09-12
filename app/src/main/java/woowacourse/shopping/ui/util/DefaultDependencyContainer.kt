package woowacourse.shopping.ui.util

import kotlin.reflect.KClass
import kotlin.reflect.KClassifier

class DefaultDependencyContainer : DependencyContainer {
    private val dependencies: MutableMap<Pair<KClassifier, String>, KClass<*>> =
        mutableMapOf()

    private val cachedInstances: MutableMap<Pair<KClassifier, String>, Any> =
        mutableMapOf()

    override fun <T : Any> getInstance(kClassifier: KClassifier, qualifier: String): T? {
        val qualifierKey = qualifier.ifEmpty { DependencyContainer.DEFAULT_QUALIFIER }
        return cachedInstances[kClassifier to qualifierKey] as T?
    }

    override fun <T : Any> getImplement(kClassifier: KClassifier, qualifier: String): KClass<T>? {
        val qualifierKey = qualifier.ifEmpty { DependencyContainer.DEFAULT_QUALIFIER }
        return dependencies[kClassifier to qualifierKey] as KClass<T>?
    }

    override fun <T : Any> setDependency(
        kClassifier: KClassifier,
        kClass: KClass<T>,
        qualifier: String
    ) {
        val qualifierKey = qualifier.ifEmpty { DependencyContainer.DEFAULT_QUALIFIER }
        dependencies[kClassifier to qualifierKey] = kClass
    }

    override fun setInstance(kClassifier: KClassifier, instance: Any, qualifier: String) {
        val qualifierKey = qualifier.ifEmpty { DependencyContainer.DEFAULT_QUALIFIER }
        if (dependencies.contains(kClassifier to qualifierKey)) {
            cachedInstances[kClassifier to qualifierKey] = instance
        }
    }
}
