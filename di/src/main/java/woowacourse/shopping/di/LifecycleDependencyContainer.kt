package woowacourse.shopping.di

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import kotlin.reflect.KClass

typealias LifecycleAwareAnnotation = KClass<*>?

interface LifecycleDependencyContainer : DependencyContainer {
    fun setLifecycle(context: Context)

    fun getLifecycleOwner(): LifecycleOwner

    fun getContext(): Context

    fun <T : Any> setDependencyWithinLifecycle(
        dependency: DependencyType,
        implementation: ImplementationClass<T>,
        qualifier: AnnotationQualifier = null,
        lifecycleAware: LifecycleAwareAnnotation = null,
    )

    fun setInstanceWithinLifecycle(
        dependency: DependencyType,
        instance: DependencyInstance,
        qualifier: AnnotationQualifier = null,
        lifecycleOwner: LifecycleOwner? = null,
        lifecycleAware: LifecycleAwareAnnotation = null,
    )
}
