package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KClassifier

class DefaultDependencyContainer : DependencyContainer {
    private val dependencies: MutableMap<Pair<KClassifier, String?>, KClass<*>> =
        mutableMapOf()

    private val cachedInstances: MutableMap<Pair<KClassifier, String?>, Any> =
        mutableMapOf()

    override fun <T : Any> getInstance(
        kClassifier: KClassifier,
        qualifier: String?,
    ): T? = cachedInstances[kClassifier to qualifier] as T?

    override fun <T : Any> getImplement(
        kClassifier: KClassifier,
        qualifier: String?,
    ): KClass<T>? = dependencies[kClassifier to qualifier] as? KClass<T>

    override fun <T : Any> setDependency(
        kClassifier: KClassifier,
        kClass: KClass<T>,
        qualifier: String?,
    ) {
        dependencies[kClassifier to qualifier] = kClass
    }

    override fun setInstance(
        kClassifier: KClassifier,
        instance: Any,
        qualifier: String?,
    ) {
        if (dependencies.contains(kClassifier to qualifier)) {
            cachedInstances[kClassifier to qualifier] = instance
        }
    }
}
