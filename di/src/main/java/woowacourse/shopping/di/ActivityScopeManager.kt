import android.app.Activity
import android.content.Context
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.qualifier.Database
import woowacourse.shopping.di.qualifier.InMemory
import woowacourse.shopping.di.qualifier.Singleton
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

object ActivityScopeManager {
    private val activityScopes = ConcurrentHashMap<Activity, ConcurrentHashMap<KClass<*>, Any>>()

    fun onActivityCreated(activity: Activity) {
        activityScopes[activity] = ConcurrentHashMap()
    }

    fun onActivityDestroyed(activity: Activity) {
        activityScopes.remove(activity)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(
        activity: Activity,
        serviceClass: KClass<T>,
    ): T {
        val scope =
            activityScopes[activity]
                ?: throw IllegalStateException("Scope for activity $activity not found. Was onActivityCreated called?")

        if (scope.containsKey(serviceClass)) {
            return scope[serviceClass] as T
        }

        val constructor = serviceClass.constructors.first()
        val args =
            constructor.parameters
                .map { parameter ->
                    val parameterType = parameter.type.classifier as KClass<*>
                    if (parameterType == Context::class) {
                        activity
                    } else {
                        val qualifier = parameter.annotations.firstOrNull { it is Database || it is InMemory }
                        val isSingleton = parameter.annotations.any { it is Singleton }
                        AppContainer.get(parameterType, qualifier, isSingleton)
                    }
                }.toTypedArray()

        val instance = constructor.call(*args)

        scope[serviceClass] = instance
        return instance
    }
}
