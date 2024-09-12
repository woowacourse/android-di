package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KClassifier

interface DependencyContainer {
    fun <T : Any> getInstance(
        kClassifier: KClassifier,
        qualifier: String,
    ): T?

    fun <T : Any> getImplement(
        kClassifier: KClassifier,
        qualifier: String,
    ): KClass<T>?

    fun <T : Any> setDependency(
        kClassifier: KClassifier,
        kClass: KClass<T>,
        qualifier: String,
    )

    fun setInstance(
        kClassifier: KClassifier,
        instance: Any,
        qualifier: String,
    )

    companion object {
        const val DEFAULT_QUALIFIER = "default"
    }
}
