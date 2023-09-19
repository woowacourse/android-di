package com.dygames.di

import com.dygames.di.annotation.Injectable
import com.dygames.di.annotation.NotCaching
import com.dygames.di.annotation.Qualifier
import com.dygames.di.error.InjectError
import com.dygames.di.model.LifecycleAwareDependencies
import com.dygames.di.model.LifecycleAwareProviders
import com.dygames.di.model.QualifiableDependencies
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf

object DependencyInjector {
    val lifecycleAwareProviders: LifecycleAwareProviders = LifecycleAwareProviders()
    val lifecycleAwareDependencies: LifecycleAwareDependencies = LifecycleAwareDependencies()

    inline fun <reified T : Any> inject(lifecycle: KType? = null): T {
        return inject(typeOf<T>(), lifecycle = lifecycle) as T
    }

    fun inject(
        type: KType,
        qualifier: Annotation? = null,
        lifecycle: KType? = null,
    ): Any {
        return findDependency(type, qualifier) ?: instantiate(type, qualifier, lifecycle)
    }

    fun createDependencies(lifecycle: KType?) {
        lifecycleAwareDependencies.value[lifecycle] = QualifiableDependencies()
    }

    fun addDependencies(type: KType, lifecycle: KType?, qualifier: Annotation?, dependency: Any) {
        lifecycleAwareDependencies.value[lifecycle]?.value?.get(qualifier)?.value?.set(
            type,
            dependency
        )
    }

    fun deleteDependencies(lifecycle: KType?) {
        lifecycleAwareDependencies.value.remove(lifecycle)
    }

    private fun instantiate(
        type: KType,
        qualifier: Annotation? = null,
        lifecycle: KType? = null
    ): Any {
        val provider = lifecycleAwareProviders.value.values.firstNotNullOfOrNull {
            val providers = it.value[qualifier] ?: return@firstNotNullOfOrNull null
            val factory = providers.factories[type]
                ?: return@firstNotNullOfOrNull providers.constructors[type]
            return factory()
        }

        val constructor =
            type.jvmErasure.primaryConstructor ?: (
                provider?.jvmErasure?.primaryConstructor
                    ?: throw InjectError.ConstructorNoneAvailable(type)
                )

        val parameters = constructor.parameters
        val arguments = gatherArguments(parameters, lifecycle)
        return constructor.call(*arguments).also {
            if (type.jvmErasure.hasAnnotation<NotCaching>()) return@also
            lifecycleAwareDependencies.value[lifecycle]?.value?.get(qualifier)?.value?.set(
                type, it
            )
        }.apply {
            injectFields(lifecycle, this)
        }
    }

    private fun findDependency(type: KType, qualifier: Annotation?): Any? {
        val qualifiableDependencies =
            lifecycleAwareDependencies.value.values.firstOrNull { qualifiableDependencies ->
                qualifiableDependencies.value.values.firstOrNull { dependencies ->
                    dependencies.value[type] != null
                } != null
            }
        val dependencies = qualifiableDependencies?.value?.get(qualifier) ?: return null
        return dependencies.value[type]
    }

    private fun gatherArguments(parameters: List<KParameter>, lifecycle: KType? = null): Array<*> {
        return parameters.map { parameter ->
            val qualifier = findQualifier(parameter.annotations)
            inject(parameter.type, qualifier, lifecycle)
        }.toTypedArray()
    }

    private fun injectFields(lifecycle: KType?, instance: Any): Any {
        val fields = instance::class.declaredMemberProperties
        fields.filter { it.hasAnnotation<Injectable>() }.filterIsInstance<KMutableProperty<*>>()
            .forEach {
                val qualifier = findQualifier(it.annotations)
                it.setter.call(instance, inject(it.returnType, qualifier, lifecycle))
            }
        return instance
    }

    private fun findQualifier(annotations: List<Annotation>): Annotation? {
        return annotations.firstOrNull {
            it.annotationClass.hasAnnotation<Qualifier>()
        }
    }
}
