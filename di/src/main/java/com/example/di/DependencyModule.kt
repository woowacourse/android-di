package com.example.di

import kotlin.reflect.KClass

class DependencyModule : DependencyProvider {
    private val instanceDependencies = hashMapOf<KClass<out Any>, Any>()
    private val deferredDependencies = hashMapOf<KClass<out Any>, MutableList<KClass<out Any>>>()

    override fun addInstanceDependency(vararg dependencies: Pair<KClass<out Any>, Any>) {
        dependencies.forEach { (requiredType, instance) ->
            instanceDependencies[requiredType] = instance
        }
    }

    override fun addDeferredDependency(vararg dependencies: Pair<KClass<out Any>, KClass<out Any>>) {
        dependencies.forEach { (requiredType, concreteType) ->
            deferredDependencies.computeIfAbsent(requiredType) { mutableListOf() }.add(concreteType)
        }
    }

    override fun getImplementationClass(
        kClass: KClass<*>,
        annotation: Annotation?,
    ): KClass<out Any> {
        val targets = requireNotNull(deferredDependencies[kClass]) { "해당 타입에 대한 의존성이 등록되지 않았습니다. 어노테이션을 확인하세요: ${kClass.simpleName}" }

        if (annotation == null) {
            if (targets.size > 1) {
                error("해당 타입에 대한 의존성이 등록되어 있으나, 주입할 수 있는 구현체가 여러 개입니다. 어노테이션을 확인하세요: ${kClass.simpleName}")
            }
            return targets.first()
        }
        return targets.findTargetWithAnnotation(annotation)
            ?: error("해당 타입에 대한 의존성이 등록되지 않았습니다. 어노테이션을 확인하세요: ${kClass.simpleName}")
    }

    override fun <T : Any> getInstance(
        kClass: KClass<*>,
        annotation: Annotation?,
    ): T? {
        if (annotation == null) return instanceDependencies[kClass] as? T
        val implementations =
            deferredDependencies[kClass]
                ?: error("해당 타입에 대한 의존성이 등록되지 않았습니다. 어노테이션을 확인하세요: ${kClass.simpleName}")
        val implementation = implementations.findTargetWithAnnotation(annotation)

        return instanceDependencies[implementation] as? T
    }

    override fun clear() {
        instanceDependencies.clear()
        deferredDependencies.clear()
    }

    private fun KClass<*>.hasAnnotation(annotationClass: KClass<out Annotation>): Boolean {
        return this.annotations.any { it.annotationClass == annotationClass }
    }

    private fun List<KClass<*>>.findTargetWithAnnotation(annotation: Annotation): KClass<*>? =
        this.find { it.hasAnnotation(annotation.annotationClass) }
}
