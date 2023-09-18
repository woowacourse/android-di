package woowacourse.shopping.container

import woowacourse.shopping.annotation.ApplicationLifecycle
import woowacourse.shopping.annotation.Lifecycle
import woowacourse.shopping.annotation.ReleasedActivityLifecycle
import woowacourse.shopping.annotation.RetainedActivityLifecycle
import woowacourse.shopping.dslbuilder.ProviderBuilder
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.hasAnnotation

class DiContainer {
    private val containers: MutableMap<String, Any> = mutableMapOf()
    private var providers: Map<KClass<*>, KFunction<*>> = emptyMap()

    fun registerProviders(block: ProviderBuilder.() -> Unit) {
        providers = ProviderBuilder().apply(block).build()
    }

    internal fun <T : Any> getSavedInstanceOf(
        annotations: List<Annotation>,
        implementationClass: KClass<out T>,
    ): T? {
        val lifecycle =
            annotations.firstOrNull { it.annotationClass.hasAnnotation<Lifecycle>() } ?: return null
        val container = when (lifecycle) {
            is ApplicationLifecycle -> {
                containers.getOrDefault("ApplicationContainer", null)
            }
            is RetainedActivityLifecycle -> {
                containers.getOrDefault("Retained" + lifecycle.activityClassName + "Container", null)
            }
            is ReleasedActivityLifecycle -> {
                containers.getOrDefault("Released" + lifecycle.activityClassName + "Container", null)
            }
            else -> throw NoWhenBranchMatchedException("저장된 인스턴스를 가져오는 함수에 ${lifecycle.annotationClass} 어노테이션 분기 처리가 존재하지 않습니다.")
        } as? LifecycleContainer
        return container?.getSavedInstanceOf(implementationClass)
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <T : Any> getProviderOf(implementationClass: KClass<out T>): KFunction<T>? {
        return providers[implementationClass] as? KFunction<T>
    }

    internal fun <T : Any> saveInstance(
        annotations: List<Annotation>,
        implementationClass: KClass<out T>,
        instance: T,
    ) {
        val lifecycle =
            annotations.firstOrNull { it.annotationClass.hasAnnotation<Lifecycle>() } ?: return
        val container = when (lifecycle) {
            is ApplicationLifecycle -> {
                containers.getOrPut("ApplicationContainer") { LifecycleContainer() }
            }
            is RetainedActivityLifecycle -> {
                containers.getOrPut("Retained" + lifecycle.activityClassName + "Container") { LifecycleContainer() }
            }
            is ReleasedActivityLifecycle -> {
                containers.getOrPut("Released" + lifecycle.activityClassName + "Container") { LifecycleContainer() }
            }
            else -> throw NoWhenBranchMatchedException("인스턴스를 저장하는 함수에 ${lifecycle.annotationClass} 어노테이션 분기 처리가 존재하지 않습니다.")
        } as LifecycleContainer
        container.saveInstance(implementationClass, instance)
    }

    internal fun <T : Any> addDependency(dependency: String, clazz: KClass<out T>, instance: T) {
        println("add dependency")
        println(dependency)
        println(instance)
        val container = containers.getOrPut(dependency) { LifecycleContainer() } as LifecycleContainer
        container.saveInstance(clazz, instance)
    }

    internal fun releaseDependency(dependency: String) {
        containers.remove(dependency)
    }
}
