package woowacourse.shopping.di.container

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import woowacourse.shopping.di.provider.ComponentDependencyProvider
import woowacourse.shopping.di.lifecycle.ComponentLifecycleObserver
import woowacourse.shopping.di.provider.DependencyProvider
import woowacourse.shopping.di.lifecycle.ViewModelLifecycleObserver
import woowacourse.shopping.di.annotation.ActivityLifecycleAware
import woowacourse.shopping.di.annotation.ApplicationLifecycleAware
import woowacourse.shopping.di.annotation.FragmentLifecycleAware
import woowacourse.shopping.di.annotation.ViewModelLifecycleAware

class ApplicationDependencyContainer(
    private val dependencyStorage: HashMap<ClassQualifier, DependencyProvider> = hashMapOf(),
) : LifecycleDependencyContainer {
    private lateinit var applicationContext: Context

    override fun <T : DependencyInstance> getInstance(
        dependency: DependencyType,
        qualifier: AnnotationQualifier,
    ): T? = dependencyStorage[ClassQualifier(dependency, qualifier)]?.getInstance()

    override fun <T : Any> getImplementation(
        dependency: DependencyType,
        qualifier: AnnotationQualifier,
    ): ImplementationClass<T>? =
        dependencyStorage[ClassQualifier(dependency, qualifier)]?.getImplementation()

    override fun <T : Any> setDependency(
        dependency: DependencyType,
        implementation: ImplementationClass<T>,
        qualifier: AnnotationQualifier,
    ) {
        val dependencyProvider = ComponentDependencyProvider()
        dependencyProvider.setDependency(implementation)
        dependencyStorage[ClassQualifier(dependency, qualifier)] = dependencyProvider
    }

    override fun setInstance(
        dependency: DependencyType,
        instance: DependencyInstance,
        qualifier: AnnotationQualifier,
    ) {
        if (dependencyStorage.contains(ClassQualifier(dependency, qualifier))) {
            dependencyStorage[ClassQualifier(dependency, qualifier)]?.setInstance(instance)
        }
    }

    override fun setApplicationContext(context: Context) {
        applicationContext = context
    }

    override fun getApplicationContext(): Context = applicationContext

    override fun setInstanceWithinLifecycle(
        dependency: DependencyType,
        instance: DependencyInstance,
        qualifier: AnnotationQualifier,
        lifecycleOwner: LifecycleOwner?,
        lifecycleAware: LifecycleAwareAnnotation,
    ) {
        val dependencyProvider: DependencyProvider =
            dependencyStorage[ClassQualifier(dependency, qualifier)]
                ?: ComponentDependencyProvider()
        dependencyProvider.setInstance(instance)
        when (lifecycleAware) {
            ApplicationLifecycleAware::class -> {}

            ActivityLifecycleAware::class -> {
                lifecycleOwner?.lifecycle?.addObserver(
                    ComponentLifecycleObserver {
                        dependencyProvider.deleteInstance()
                    },
                )
            }

            FragmentLifecycleAware::class -> {
                lifecycleOwner?.lifecycle?.addObserver(
                    ComponentLifecycleObserver(
                        onFinish = {
                            dependencyProvider.deleteInstance()
                        }
                    ),
                )
            }

            ViewModelLifecycleAware::class -> {
                lifecycleOwner?.lifecycle?.addObserver(
                    ViewModelLifecycleObserver(
                        onCreate = { dependencyProvider.setInstance(instance) },
                        onFinish = { dependencyProvider.deleteInstance() },
                    ),
                )
            }

            else -> {}
        }
    }
}
