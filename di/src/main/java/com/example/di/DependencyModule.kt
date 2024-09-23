package com.example.di

import kotlin.reflect.KClass

object DependencyModule {
    private val instanceDependencies = hashMapOf<KClass<out Any>, Any>()
    private val deferredDependencies = hashMapOf<KClass<out Any>, MutableList<KClass<out Any>>>()
    private val singleTonDependencies = hashMapOf<KClass<out Any>, MutableList<KClass<out Any>>>()

    fun addInstanceDependency(vararg dependencies: Pair<KClass<out Any>, Any>) {
        dependencies.forEach { (requiredType, instance) ->
            instanceDependencies[requiredType] = instance
        }
    }

    fun addDeferredDependency(vararg dependencies: Pair<KClass<out Any>, KClass<out Any>>) {
        dependencies.forEach { (requiredType, concreteType) ->
            deferredDependencies.computeIfAbsent(requiredType) { mutableListOf() }.add(concreteType)
        }
    }

    fun addSingletonDependency(vararg dependencies: Pair<KClass<out Any>, KClass<out Any>>) {
        dependencies.forEach { (requiredType, concreteType) ->
            singleTonDependencies.computeIfAbsent(requiredType) { mutableListOf() }.add(concreteType)
            deferredDependencies.computeIfAbsent(requiredType) { mutableListOf() }.add(concreteType)
        }
    }

    fun getSingletonInstances() = instanceDependencies.toMap()

    fun getSingletonDependencies() = singleTonDependencies.entries.flatMap { it.value }

    fun getImplementationClass(
        kClass: KClass<*>,
        annotation: Annotation? = null,
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

    private fun KClass<*>.hasAnnotation(annotationClass: KClass<out Annotation>): Boolean {
        return this.annotations.any { it.annotationClass == annotationClass }
    }

    private fun List<KClass<*>>.findTargetWithAnnotation(annotation: Annotation): KClass<*>? =
        this.find { it.hasAnnotation(annotation.annotationClass) }
}
