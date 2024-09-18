package olive.di

import kotlin.reflect.KClass

class NamedInstances(
    private val instances: MutableMap<KClass<*>, MutableList<NamedInstance>> = mutableMapOf(),
) {
    fun instanceByName(
        qualifierAnnotation: KClass<out Annotation>,
        type: KClass<*>,
    ): Any? {
        return get(type)?.find { it.qualifierAnnotation == qualifierAnnotation }?.instance
    }

    operator fun get(type: KClass<*>): MutableList<NamedInstance>? {
        return instances[type]
    }

    operator fun set(
        type: KClass<*>,
        instance: NamedInstance,
    ) {
        instances.getOrPut(type) { mutableListOf() }.add(instance)
    }
}
