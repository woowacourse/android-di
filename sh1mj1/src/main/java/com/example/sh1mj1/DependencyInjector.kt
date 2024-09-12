package com.example.sh1mj1

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class DependencyInjector(
    private val appContainer: AppContainer,
) {
    fun <T : Any> createInstance(modelClass: Class<T>): T {
        val kClass = modelClass.kotlin

        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("ViewModel must have a primary constructor: ${kClass.simpleName}")

        val injectedFields = kClass.memberProperties.filter { it.hasAnnotation<Inject>() }

        val constructorArgs =
            constructor.parameters.map { parameter ->
                appContainer.find(parameter.type.classifier as KClass<*>)
            }.toTypedArray()

        val viewModel = constructor.call(*constructorArgs)

        injectedFields.forEach { field ->
            field.isAccessible = true // private 필드에 접근할 수 있도록 설정

            val qualifier =
                field.annotations.filterIsInstance<Qualifier>().firstOrNull()

            val dependency =
                if (qualifier != null) {
                    appContainer.find(field.returnType.classifier as KClass<*>, qualifier)
                } else {
                    appContainer.find(field.returnType.classifier as KClass<*>)
                } ?: throw IllegalArgumentException("Unresolved dependency for field: ${field.name}")

            if (field is KMutableProperty<*>) {
                field.setter.call(viewModel, dependency)
            } else {
                throw IllegalArgumentException("Field ${field.name} is not mutable and cannot be injected")
            }
        }

        return viewModel
    }

    // 필드에 대한 의존성 주입
    private fun <T : Any> injectFields(instance: T) {
        val kClass = instance::class

        // @Inject가 있는 필드를 찾음
        val injectedFields = kClass.memberProperties.filter { it.hasAnnotation<Inject>() }

        injectedFields.forEach { field ->
            field.isAccessible = true // private 필드에 접근할 수 있도록 설정

            // 필드에 붙은 Qualifier 찾기
            val qualifier = field.annotations.filterIsInstance<Qualifier>().firstOrNull()

            // Qualifier에 따른 의존성 검색
            val dependency =
                if (qualifier != null) {
                    appContainer.find(field.returnType.classifier as KClass<*>, qualifier)
                } else {
                    appContainer.find(field.returnType.classifier as KClass<*>)
                } ?: throw IllegalArgumentException("Unresolved dependency for field: ${field.name}")

            // 필드에 의존성 할당
            if (field is KMutableProperty<*>) {
                field.setter.call(instance, dependency)
            } else {
                throw IllegalArgumentException("Field ${field.name} is not mutable and cannot be injected")
            }
        }
    }
}
