package com.bandal.di

import kotlin.reflect.KClass

object BandalInjectorAppContainer : AppContainer {

    private val instances: HashMap<Pair<KClass<*>, List<Annotation>>, Any> = HashMap()

    override fun getInstance(type: KClass<*>, annotations: List<Annotation>): Any? {
        return instances.keys.firstOrNull { (clazz, annotationList) ->
            clazz == type && annotationList.containsAll(annotations)
        }?.let { instances[it] }
    }

    override fun addInstance(type: KClass<*>, clazz: KClass<*>) {
        val annotationWithQualifier = clazz.annotations.filter { annotation ->
            annotation.annotationClass.java.isAnnotationPresent(
                Qualifier::class.java,
            )
        }
        val key = Pair(type, annotationWithQualifier)
        instances[key] = BandalInjector.inject(clazz)
    }

    override fun addInstance(type: KClass<*>, instance: Any) {
        val annotationWithQualifier = instance::class.annotations.filter { annotation ->
            annotation.annotationClass.java.isAnnotationPresent(
                Qualifier::class.java,
            )
        }
        val key = Pair(type, annotationWithQualifier)
        instances[key] = instance
    }

    override fun clear() {
        instances.clear()
    }
}
