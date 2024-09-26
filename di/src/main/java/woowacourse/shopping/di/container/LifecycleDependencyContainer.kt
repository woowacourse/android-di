package woowacourse.shopping.di.container

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import kotlin.reflect.KClass

typealias LifecycleAwareAnnotation = KClass<*>?

interface LifecycleDependencyContainer : DependencyContainer {
    fun setApplicationContext(context: Context)

    fun getApplicationContext(): Context

    fun setInstanceWithinLifecycle(
        dependency: DependencyType,
        instance: DependencyInstance,
        qualifier: AnnotationQualifier = null,
        lifecycleOwner: LifecycleOwner? = null,
        lifecycleAware: LifecycleAwareAnnotation = null,
    )
}
