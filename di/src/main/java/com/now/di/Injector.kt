package com.now.di

import com.now.annotation.Inject
import com.now.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class Injector(
    private val container: Container,
) {
    // 인자로 받은 모듈에 있는 메서드를 인스턴스화 하여 Container에 저장한다
    fun addModule(module: Module) {
        val kFunctions = module::class.declaredFunctions
        kFunctions.forEach { kFunction ->
            container.addInstance(module, kFunction)
        }
    }

    fun getCurrentContainer(): Container {
        return container
    }

    // klass의 인스턴스를 생성하여 반환한다
    fun <T : Any> inject(klass: KClass<T>): T {
        //  주생성자를 가져온다
        val primaryConstructor =
            klass.primaryConstructor ?: throw NullPointerException("주 생성자가 없습니다.")

        // 인자들 중 Inject 어노테이션 붙은 인자들만 가져온다
        val parameters = primaryConstructor.parameters.filter { it.hasAnnotation<Inject>() }

        // 주생성자의 인자들을 Container에서 가져온다
        val insertedParameters = parameters.associateWith { kParameter ->
            val type = kParameter.type.jvmErasure
            val annotation = kParameter.annotations.firstOrNull { _annotation ->
                _annotation.annotationClass.hasAnnotation<Qualifier>()
            }
            container.getInstanceRecursive(DependencyType(type, annotation))
        }

        val instance = primaryConstructor.callBy(insertedParameters)
        injectParams(klass, instance)
        return instance
    }

    // 주입이 필요하지만 생성자에 위치하지 않은 프로퍼티
    fun injectParams(klass: KClass<*>, instance: Any) {
        val properties =
            klass.declaredMemberProperties.filterIsInstance<KMutableProperty1<*, *>>()
                .filter { it.hasAnnotation<Inject>() }
        properties.forEach {
            it.isAccessible = true
            val dependencyType = DependencyType(it.returnType.jvmErasure, it.findAnnotation<Qualifier>())
            it.setter.call(instance, container.getInstanceRecursive(dependencyType))
        }
    }
}
