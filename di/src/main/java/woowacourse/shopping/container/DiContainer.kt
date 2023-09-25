package woowacourse.shopping.container

import woowacourse.shopping.Module
import woowacourse.shopping.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

class DiContainer {
    private val containers: MutableMap<String, Any> = mutableMapOf()
    private var providers: HashMap<Pair<KClass<*>, Annotation?>, Any?> = HashMap()
    private var modules: Map<KFunction<*>, Module> = emptyMap()

    internal fun registerModule(module: Module) {
        modules = module::class.declaredMemberFunctions.associateWith { module }
        module::class.declaredMemberFunctions.forEach {
            val kClass = it.returnType.jvmErasure
            val annotation = it.annotations.firstOrNull {
                it.annotationClass.hasAnnotation<Qualifier>()
            }
            val key = kClass to annotation
            providers[key] = createInstance(it, module)
            println("0 " + createInstance(it, module))
        }
    }

    internal fun <T : Any> getSavedInstanceOf(
        annotations: List<Annotation>,
        implementationClass: KClass<out T>,
    ): T? {
        val container = containers.getOrDefault(
            implementationClass.simpleName.toString(),
            null,
        ) as? LifecycleContainer
        val annotation = annotations.firstOrNull {
            it.annotationClass.hasAnnotation<Qualifier>()
        }
        return container?.getSavedInstanceOf(implementationClass, annotation)
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <T : Any> getProviderOf(
        implementationClass: KClass<out T>,
        annotation: Annotation?,
    ): T? {
        val key = implementationClass to annotation
        return providers[key] as? T
    }

    private fun createInstance(provideFunc: KFunction<*>, receiver: Module): Any? {
        val injectParams = provideFunc.parameters
        if (injectParams.isNotEmpty()) {
            val args = injectParams.associateWith { param ->
                val clazz = param.type.jvmErasure
                val qualifier = param.annotations.firstOrNull {
                    it.annotationClass.hasAnnotation<Qualifier>()
                }
                providers[clazz to qualifier]
            }.toMutableMap()
            args[provideFunc.parameters.first()] = receiver
            return provideFunc.callBy(args)
        }
        return provideFunc.callBy(mapOf(provideFunc.parameters.first() to receiver))
    }

    internal fun <T : Any> saveInstance(
        annotations: List<Annotation>,
        implementationClass: KClass<out T>,
        instance: T,
    ) {
        val container =
            containers.getOrPut(implementationClass.simpleName.toString()) { LifecycleContainer() } as LifecycleContainer
        val annotation = annotations.firstOrNull {
            it.annotationClass.hasAnnotation<Qualifier>()
        }
        container.saveInstance(implementationClass, annotation, instance)
    }

    internal fun <T : Any> addDependency(dependency: String, clazz: KClass<out T>, instance: T) {
        val container =
            containers.getOrPut(dependency) { LifecycleContainer() } as LifecycleContainer
        val annotation = clazz.annotations.firstOrNull {
            it.annotationClass.hasAnnotation<Qualifier>()
        }
        container.saveInstance(clazz, annotation, instance)
    }

    internal fun releaseDependency(dependency: String) {
        containers.remove(dependency)
    }
}
