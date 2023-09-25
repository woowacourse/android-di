package woowacourse.shopping.container

import kotlin.reflect.KClass

class LifecycleContainer {
    private val instances: MutableMap<Pair<KClass<*>, Annotation?>, Any> = mutableMapOf()

    internal fun <T : Any> saveInstance(
        clazz: KClass<out T>,
        annotation: Annotation?,
        instance: T,
    ) {
        val key = clazz to annotation
        instances[key] = instance
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <T : Any> getSavedInstanceOf(clazz: KClass<out T>, annotation: Annotation?): T? {
        val key = clazz to annotation
        return instances[key] as? T
    }
}
