package com.example.di

import javax.inject.Singleton
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class DependencyInjector(private val registry: DiContainer) {
    fun inject(classType: KClass<*>): Any {
        val isSingleton = classType.findAnnotation<Singleton>() != null
        return registry.getInstanceOrNull(classType)?.takeIf { isSingleton } ?: createInstance(
            classType,
        )
    }

    private fun createInstance(classType: KClass<*>): Any {
        val constructor = getPrimaryConstructor(classType)
        val parameterDependencies = resolveConstructorParameters(constructor.parameters)
        val instance = constructor.callBy(parameterDependencies)
        injectFields(instance)
        registry.addInstance(classType, instance)
        return instance
    }

    private fun <T : Any> getPrimaryConstructor(classType: KClass<T>): KFunction<Any> {
        return classType.primaryConstructor
            ?: throw IllegalArgumentException("주생성자를 찾을 수 없습니다: ${classType.simpleName}")
    }

    private fun resolveConstructorParameters(parameters: List<KParameter>): Map<KParameter, Any?> {
        return parameters.associateWith { parameter ->
            val targetClassType =
                findQualifiedAnnotationOrNull(parameter)
                    ?: (parameter.type.classifier as? KClass<*>)
            targetClassType?.let { clazz ->
                inject(clazz)
            }
        }
    }

    // target 인스턴스에서 @Inject가 붙은 필드를 찾아 injectProperty로 필드 주입을 수행한다
    private fun <T : Any> injectFields(target: T) {
        target::class.declaredMemberProperties
            .filter { kProperty ->
                kProperty.findAnnotation<Inject>() != null && kProperty is KMutableProperty1<*, *>
            }.forEach { kProperty ->
                val mutableProperty = kProperty as KMutableProperty1<out T, *>
                injectProperty(target, mutableProperty)
            }
    }

    // target 인스턴스와 mutableProperty의 의존성을 해소한다.
    private fun <T : Any> injectProperty(
        target: T,
        mutableProperty: KMutableProperty1<out T, *>,
    ) {
        val targetClassType =
            findQualifiedAnnotationOrNull(mutableProperty)
                ?: mutableProperty.returnType.classifier as? KClass<*>
        targetClassType?.let { kClass ->
            val instance = inject(kClass)
            mutableProperty.isAccessible = true
            mutableProperty.setter.call(target, instance)
        }
    }

    // @Qualified 어노테이션이 있으면 명시된 구현체 타입 정보를, 없으면 null을 반환한다
    private fun findQualifiedAnnotationOrNull(parameter: KParameter): KClass<*>? {
        val qualifier = parameter.findAnnotation<Qualifier>()
        return qualifier?.injectedClassType
    }

    private fun findQualifiedAnnotationOrNull(property: KProperty<*>): KClass<*>? {
        return property.findAnnotation<Qualifier>()?.injectedClassType
    }
}
