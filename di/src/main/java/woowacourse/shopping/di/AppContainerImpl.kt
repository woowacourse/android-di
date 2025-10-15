package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KType

class AppContainerImpl : Container {
    private val registry = mutableMapOf<Pair<KType, KClass<out Annotation>?>, (Container) -> Any>()

    fun <T : Any> register(
        type: KType,
        qualifier: Annotation? = null,
        factory: (Container) -> T,
    ) {
        registry[type to qualifier?.annotationClass] = factory as (Container) -> Any
    }

    override fun resolve(
        requestedType: KType,
        qualifier: Annotation?,
    ): Any? {
        val factory = registry[requestedType to qualifier?.annotationClass] ?: return null
        return factory(this)
    }
}
