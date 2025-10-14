package woowacourse.shopping.di

import kotlin.reflect.KType

class AppContainerImpl : Container {
    private val registry: MutableMap<Pair<KType, Annotation?>, (Container) -> Any> = mutableMapOf()

    fun <T : Any> register(
        type: KType,
        qualifier: Annotation? = null,
        factory: (Container) -> T,
    ) {
        registry[Pair(type, qualifier)] = factory as (Container) -> Any
    }

    override fun resolve(
        requestedType: KType,
        qualifier: Annotation?,
    ): Any? {
        val key = Pair(requestedType, qualifier)
        val factoryFunction = registry[key] ?: return null

        return factoryFunction(this)
    }
}
