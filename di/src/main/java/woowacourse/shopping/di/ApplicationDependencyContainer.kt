package woowacourse.shopping.di

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner

typealias ClassQualifier = Pair<DependencyType, AnnotationQualifier>

class ApplicationDependencyContainer(
    private val dependencies: MutableMap<ClassQualifier, ImplementationClass<*>> =
        mutableMapOf(),
    private val cachedInstances: MutableMap<ClassQualifier, DependencyInstance> =
        mutableMapOf(),
    private val lifecycleAwareInstances: MutableMap<ClassQualifier, DependencyProvider> = mutableMapOf(),
) : LifecycleDependencyContainer {
    private lateinit var applicationLifecycle: LifecycleOwner
    private lateinit var applicationContext: Context

    override fun <T : DependencyInstance> getInstance(
        dependency: DependencyType,
        qualifier: AnnotationQualifier,
    ): T? = cachedInstances[dependency to qualifier] as T?

    override fun <T : Any> getImplement(
        dependency: DependencyType,
        qualifier: AnnotationQualifier,
    ): ImplementationClass<T>? = dependencies[dependency to qualifier] as? ImplementationClass<T>

    override fun <T : Any> setDependency(
        dependency: DependencyType,
        implementation: ImplementationClass<T>,
        qualifier: AnnotationQualifier,
    ) {
        dependencies[dependency to qualifier] = implementation
    }

    override fun setInstance(
        dependency: DependencyType,
        instance: DependencyInstance,
        qualifier: AnnotationQualifier,
    ) {
        if (dependencies.contains(dependency to qualifier)) {
            cachedInstances[dependency to qualifier] = instance
        }
    }

    override fun setLifecycle(context: Context) {
        applicationLifecycle = context as LifecycleOwner
        applicationContext = context.applicationContext
    }

    override fun getLifecycleOwner(): LifecycleOwner = applicationLifecycle

    override fun getContext(): Context = applicationContext

    override fun <T : Any> setDependencyWithinLifecycle(
        dependency: DependencyType,
        implementation: ImplementationClass<T>,
        qualifier: AnnotationQualifier,
        lifecycleAware: LifecycleAwareAnnotation,
    ) {

    }

    override fun setInstanceWithinLifecycle(
        dependency: DependencyType,
        instance: DependencyInstance,
        qualifier: AnnotationQualifier,
        lifecycleOwner: LifecycleOwner?,
        lifecycleAware: LifecycleAwareAnnotation,
    ) {
        Log.d("hodu", "setInstanceWitinLifecycle")
        Log.d("hodu", "lifecycleOwner: $lifecycleOwner")
        Log.d("hodu", "lifecycleAware: $lifecycleAware")
        val dependencyProvider = ComponentDependencyProvider().apply {
            setInstance(instance)
        }
        lifecycleAwareInstances[dependency to qualifier] = dependencyProvider
        when (lifecycleAware) {
            ApplicationLifecycleAware::class -> {
                // 어플리케이션 lifecycle에 aware
                applicationLifecycle.lifecycle.addObserver(ComponentLifecycleObserver { dependencyProvider.deleteInstance() })
                Log.d("hodu", "어플리케이션 lifecycle에 옵저버 등록")
            }

            ViewModelLifecycleAware::class -> {
                // 뷰모델 lifecycle에 aware
                lifecycleOwner?.lifecycle?.addObserver(ViewModelLifecycleObserver { dependencyProvider.deleteInstance() })
                Log.d("hodu", "뷰모델 lifecycle에 옵저버 등록")
            }

            FragmentLifecycleAware::class -> {
                // 프래그먼트 lifecycle에 aware
                lifecycleOwner?.lifecycle?.addObserver(ComponentLifecycleObserver { dependencyProvider.deleteInstance() })
                Log.d("hodu", "프래그먼트 lifecycle에 옵저버 등록")
            }

            else -> {}
        }
    }
}
