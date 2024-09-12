package woowacourse.shopping.ui.util

import kotlin.reflect.KClass
import kotlin.reflect.KClassifier

interface DependencyContainer {
    fun <T : Any> getInstance(kClassifier: KClassifier): T?

    fun <T : Any> getImplement(kClassifier: KClassifier): KClass<T>?

    fun <T : Any> setDependency(kClassifier: KClassifier, kClass: KClass<T>)

    fun setInstance(kClassifier: KClassifier, instance: Any)
}