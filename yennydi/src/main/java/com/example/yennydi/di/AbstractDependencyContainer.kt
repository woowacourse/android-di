package com.example.yennydi.di

import kotlin.reflect.KClass

abstract class AbstractDependencyContainer : DependencyContainer {
    protected val deferred = hashMapOf<KClass<out Any>, MutableSet<KClass<out Any>>>()

    private val instances = hashMapOf<KClass<out Any>, Any>()

    override fun <T : Any> getInstance(
        kClass: KClass<*>,
        annotation: Annotation?,
    ): T? {
        if (annotation == null) instances[kClass]?.let { return it as? T }
        val implements = getImplementationClass(kClass, annotation)
        return instances[implements] as? T
    }

    override fun getImplementationClass(
        kClass: KClass<*>,
        annotation: Annotation?,
    ): KClass<out Any>? {
        val targets = deferred[kClass] ?: return null
        if (annotation == null) {
            if (targets.size > 1) {
                error("해당 타입에 대한 구현체가 여러 개입니다. 어노테이션을 확인하세요: ${kClass.simpleName}")
            }
            return targets.first()
        }
        return targets.findTargetWithAnnotation(annotation)
            ?: error("해당 타입에 대한 의존성이 등록되지 않았습니다. 어노테이션을 확인하세요: ${kClass.simpleName}")
    }

    override fun <T : Any> addInstance(
        kClass: KClass<*>,
        instance: T,
    ) {
        instances[kClass] = instance
    }

    override fun addDeferredDependency(vararg dependencies: Pair<KClass<out Any>, KClass<out Any>>) {
        dependencies.forEach { (requiredType, concreteType) ->
            deferred.computeIfAbsent(requiredType) { mutableSetOf() }.add(concreteType)
        }
    }

    override fun clear() {
        instances.clear()
        deferred.clear()
    }

    private fun KClass<*>.hasAnnotation(annotationClass: KClass<out Annotation>): Boolean {
        return this.annotations.any { it.annotationClass == annotationClass }
    }

    private fun Set<KClass<*>>.findTargetWithAnnotation(annotation: Annotation): KClass<*>? =
        this.find {
            it.hasAnnotation(annotation.annotationClass)
        }
}
