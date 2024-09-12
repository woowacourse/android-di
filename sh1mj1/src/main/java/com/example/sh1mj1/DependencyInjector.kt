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

        val constructorArgs =
            constructor.parameters.map { parameter ->
                appContainer.find(parameter.type.classifier as KClass<*>)
            }.toTypedArray()

        // inject 를 해야하는 생성자 인자 찾기
        val constructorArgs2 = constructor.parameters.filter { kParameter ->
            kParameter.hasAnnotation<Inject>()
        }


        val x = constructorArgs2.map { kParameter ->
            val qualifier = kParameter.annotations.filterIsInstance<Qualifier>().firstOrNull().also {
                println("qualifier1: $it")
            }
            val dependency = if (qualifier != null) {
                appContainer.find(kParameter.type.classifier as KClass<*>, qualifier).also {
                    println("Tag $it")
                }
            } else {
                appContainer.find(kParameter.type.classifier as KClass<*>)
            }
            dependency ?: throw IllegalArgumentException("Unresolved dependency for parameter: ${kParameter.name}")
        }.toTypedArray()

//        val viewModel = constructor.call(*constructorArgs)
        val viewModel = constructor.call(*x)

        // inject 가 있는 필드 찾기
        val injectedFields = kClass.memberProperties.filter { it.hasAnnotation<Inject>() }

        injectedFields.forEach { field ->
            field.isAccessible = true

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

    fun asdfasdf(qualifier: Qualifier) {

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
