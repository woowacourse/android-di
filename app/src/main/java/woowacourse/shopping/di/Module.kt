package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class Module : DependencyProvider {
    private val deferredTypes = hashMapOf<KClass<out Any>, KClass<out Any>>()
    private val cachedInstances = hashMapOf<KClass<out Any>, Any>()

    fun addDeferredTypes(vararg deferred: Pair<KClass<out Any>, KClass<out Any>>) {
        deferred.forEach { (key, deferredType) ->
            deferredTypes[key] = deferredType
        }
    }

    fun addInitializedInstances(vararg initialized: Pair<KClass<out Any>, Any>) {
        initialized.forEach { (key, instance) ->
            cachedInstances[key] = instance
        }
    }

    override fun <T : Any> getInstance(key: KClass<*>): T? {
        return cachedInstances[key]?.let { it as? T } ?: createCachedInstance(key)
    }

    private fun <T : Any> createCachedInstance(key: KClass<*>): T? {
        val typeToInstantiate = deferredTypes[key] ?: return null
        val constructor = requireNotNull(typeToInstantiate.primaryConstructor) { "No suitable constructor found for $typeToInstantiate" }

        val parameters =
            constructor.parameters
                .filter { it.hasAnnotation<Inject>() }
                .associateWith { kParameter ->
                    val paramType = kParameter.type.jvmErasure
                    cachedInstances[paramType]?.let { it as? T } ?: createCachedInstance(paramType)
                }

        val instance = constructor.callBy(parameters)
        cachedInstances[key] = instance
        return instance as? T
    }
}
