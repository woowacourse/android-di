package com.now.di

import com.now.annotation.Inject
import com.now.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class Injector(
    private val parentContainer: Container?,
    private val container: Container,
) {
    // 인자로 받은 모듈에 있는 메서드를 인스턴스화 하여 Container에 저장한다
    fun addModule(module: Module) {
        val kFunctions = module::class.declaredFunctions
        kFunctions.forEach { kFunction ->
            container.addInstance(module, kFunction)
        }
    }

    private fun getPropertyInstance(
        kProperty1: KProperty1<*, *>,
    ): Any {
        val klass = kProperty1.returnType.jvmErasure
        val annotation = kProperty1.findAnnotation<Qualifier>()
        val dependencyType = DependencyType(klass, annotation)

        return container.getInstance(dependencyType) ?: NullPointerException("컨테이너에 저장되어 있지 않습니다.")
    }

    // klass의 인스턴스를 생성하여 반환한다
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> inject(klass: KClass<*>): T {
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
            container.getInstance(DependencyType(type, annotation))
                ?: parentContainer?.getInstance(DependencyType(type, annotation))
                ?: throw IllegalArgumentException()
        }

        val instance = primaryConstructor.callBy(insertedParameters) as T
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
            it.setter.call(instance, getPropertyInstance(it))
        }
    }

    fun getCurrentContainer(): Container {
        return container
    }
}
