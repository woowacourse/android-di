package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation

private data class DependencyKey(
    val type: KType,
    val qualifier: KClass<out Annotation>?,
)

class AppContainerImpl : Container {
    private val registry = mutableMapOf<DependencyKey, (Container) -> Any>()
    private val singletonInstances = mutableMapOf<DependencyKey, Any>()

    fun <T : Any, Impl : T> register(
        type: KType,
        implementationClass: KClass<Impl>,
        qualifier: Annotation? = null,
        factory: (Container) -> Impl,
    ) {
        val key = DependencyKey(type, qualifier?.annotationClass)
        registry[key] = factory as (Container) -> Any

        if (implementationClass.findAnnotation<Singleton>() != null) {
            if (!singletonInstances.containsKey(key)) {
                singletonInstances[key] = factory(this)
            }
        }
    }

    override fun resolve(
        requestedType: KType,
        qualifier: Annotation?,
    ): Any? {
        val key = DependencyKey(requestedType, qualifier?.annotationClass)

        val singleton = singletonInstances[key]
        if (singleton != null) {
            return singleton
        }

        val factory = registry[key] ?: return null
        return factory(this)
    }
}
