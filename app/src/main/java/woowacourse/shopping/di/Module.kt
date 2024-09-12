package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class Module : DependencyProvider {
    private val deferredTypes = hashMapOf<KClass<out Any>, KClass<out Any>>()
    private val cachedInstances = hashMapOf<Pair<KClass<out Any>, DependencyType?>, Any>()
    private val annotationTypes = hashMapOf<Pair<KClass<out Any>, DependencyType>, KClass<out Any>>()

    fun addDeferredTypes(vararg deferred: Pair<KClass<out Any>, KClass<out Any>>) {
        deferred.forEach { (abstractType, concreteType) ->
            if (concreteType.hasAnnotation<Qualifier>()) {
                val qualifier = concreteType.findAnnotation<Qualifier>() ?: return
                val key = Pair(abstractType, qualifier.type)
                annotationTypes[key] = concreteType
            } else {
                deferredTypes[abstractType] = concreteType
            }
        }
    }

    fun addInitializedInstances(vararg initialized: Pair<KClass<out Any>, Any>) {
        initialized.forEach { (abstractType, instance) ->
            val qualifier = instance::class.findAnnotation<Qualifier>()?.type
            val key = Pair(abstractType, qualifier)
            cachedInstances[key] = instance
        }
    }

    override fun <T : Any> getInstance(kClass: KClass<*>): T? {
        val key = Pair(kClass, null)
        println("getInstance1: ${kClass.simpleName}")
        return cachedInstances[key]?.let { it as? T } ?: createCachedInstance(kClass, null)
    }

    override fun <T : Any> getInstance(
        kClass: KClass<*>,
        type: DependencyType,
    ): T? {
        val key = Pair(kClass, type)
        return cachedInstances[key]?.let { it as? T } ?: createCachedInstance(kClass, type)
    }

    private fun <T : Any> createCachedInstance(
        kClass: KClass<*>,
        type: DependencyType?,
    ): T? {
        val typeToInstantiate =
            if (type == null) {
                deferredTypes[kClass]
            } else {
                val key = Pair(kClass, type)
                annotationTypes[key]
            } ?: return null
        val constructor = requireNotNull(typeToInstantiate.primaryConstructor) { "No suitable constructor found for $typeToInstantiate" }

        val parameters =
            constructor.parameters
                .filter { it.hasAnnotation<Inject>() }
                .associateWith { kParameter ->
                    val paramType = kParameter.type.jvmErasure
                    val qualifier = paramType.findAnnotation<Qualifier>()?.type
                    val key = Pair(paramType, qualifier)
                    cachedInstances[key]?.let { it as? T } ?: createCachedInstance(paramType, type)
                }

        val instance = constructor.callBy(parameters)
        cachedInstances[Pair(kClass, type)] = instance
        println("getInstance2: $instance")
        return instance as? T
    }
}
