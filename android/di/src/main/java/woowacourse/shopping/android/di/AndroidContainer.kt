package woowacourse.shopping.android.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import woowacourse.shopping.core.di.DependencyContainer
import kotlin.reflect.KClass

object AndroidContainer {
    private val appContainer = DependencyContainer()
    private val activityContainers = mutableMapOf<ComponentActivity, DependencyContainer>()
    private val viewModelContainers = mutableMapOf<ViewModel, DependencyContainer>()

    fun <T : Any> register(
        clazz: KClass<T>,
        scope: Scope,
        qualifier: String? = null,
        provider: () -> T,
    ) {
        when (scope) {
            is Scope.ApplicationScope -> appContainer.register(clazz, qualifier, provider)
            is Scope.ActivityScope -> {
                val container = activityContainers[scope.owner] ?: scope.owner.newContainer()
                container.register(clazz, qualifier, provider)
            }

            is Scope.ViewModelScope -> {
                val container = viewModelContainers[scope.owner] ?: scope.owner.newContainer()
                container.register(clazz, qualifier, provider)
            }
        }
    }

    fun <T : Any> instance(
        clazz: KClass<T>,
        scope: Scope,
        qualifier: String? = null,
    ): T =
        when (scope) {
            is Scope.ApplicationScope ->
                appContainer.instance(clazz, qualifier)

            is Scope.ActivityScope -> {
                val container =
                    activityContainers[scope.owner]
                        ?: error("No container found for ${scope.owner::class.simpleName}")
                container.instance(clazz, qualifier)
            }

            is Scope.ViewModelScope -> {
                val container =
                    viewModelContainers[scope.owner]
                        ?: error("No container found for ${scope.owner::class.simpleName}")
                container.instance(clazz, qualifier)
            }
        }

    private fun ComponentActivity.newContainer(): DependencyContainer {
        val container = DependencyContainer()
        lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    activityContainers.remove(this@newContainer)
                }
            },
        )
        activityContainers[this] = container
        return container
    }

    private fun ViewModel.newContainer(): DependencyContainer {
        val container = DependencyContainer()
        addCloseable { viewModelContainers.remove(this@newContainer) }
        viewModelContainers[this] = container
        return container
    }
}
