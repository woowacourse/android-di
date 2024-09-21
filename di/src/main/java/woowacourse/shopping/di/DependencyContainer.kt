package woowacourse.shopping.di

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier

typealias DependencyType = KClassifier
typealias AnnotationQualifier = KAnnotatedElement?
typealias ImplementationClass<T> = KClass<T>
typealias DependencyInstance = Any

interface DependencyContainer {
    fun <T : DependencyInstance> getInstance(
        dependency: DependencyType,
        qualifier: AnnotationQualifier = null,
    ): T?

    fun <T : Any> getImplement(
        dependency: DependencyType,
        qualifier: AnnotationQualifier = null,
    ): ImplementationClass<T>?

    fun <T : Any> setDependency(
        dependency: DependencyType,
        implementation: ImplementationClass<T>,
        qualifier: AnnotationQualifier = null,
    )

    fun setInstance(
        dependency: DependencyType,
        instance: DependencyInstance,
        qualifier: AnnotationQualifier = null,
    )
}
