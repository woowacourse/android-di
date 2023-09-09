package com.buna.di.injector

import com.buna.di.annotation.Inject
import com.buna.di.util.validateHasPrimaryConstructor
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

object DependencyInjector {
    private val typeConverter = mutableMapOf<DependencyKey, KType>()
    private val cache = mutableMapOf<DependencyKey, Any?>()

    // 모듈로 주입 (모듈 내 Provider 함수로 객체를 생성하여 저장)
    fun module(module: Module) {
        val providers = module::class.declaredMemberFunctions
        providers.forEach { provider -> caching(module, provider) }
    }

    // 함수로 객체 만들어서 주입
    private fun caching(module: Module, provider: KFunction<*>) {
        val dependencyKey = DependencyKey.createDependencyKey(provider)
        val dependency = provider.call(module)

        cache[dependencyKey] = dependency
    }

    // value == null 이면 가져올 때 생성할 예정
    fun type(superClass: KClass<*>, subClass: KClass<*>) {
        val superType = superClass.starProjectedType
        val annotation = subClass.annotations.firstOrNull()
        val dependencyKey = DependencyKey(superType, annotation)

        val subType = subClass.starProjectedType
        typeConverter[dependencyKey] = subType
        cache[dependencyKey] = null
    }

    // 외부로 종속 항목 제공
    fun <T : Any> inject(clazz: KClass<T>): T {
        // 1. container에 있으면 바로 제공
        val type = clazz.supertypes.getOrElse(0) { clazz.starProjectedType }
        val annotation = clazz.annotations.firstOrNull()
        val dependencyKey = DependencyKey(type, annotation)

        if (cache[dependencyKey] != null) {
            return cache[dependencyKey] as T
        }

        // 없으면 클래스의 주생성자 파라미터 순회
        val primaryConstructor = clazz.validateHasPrimaryConstructor()
        val parameters = primaryConstructor.parameters

        // 1. 파라미터를 순회하며 inject() 재귀 호출
        val arguments = parameters.map { parameter ->
            // 현재 문제 : 인터페이스 타입을 넘기는 경우 인터페이스 타입이 아닌 구현체 타입을 넘겨야 함
            val paramAnnotation = parameter.annotations.firstOrNull()
            val paramType = parameter.type // 인터페이스 타입일 수도 있음
            val paramDependencyKey = DependencyKey(paramType, paramAnnotation)
            val subType = typeConverter[paramDependencyKey] ?: paramType
            val paramInstance = inject(subType.jvmErasure)

            paramInstance
        }

        // 2. 파라미터를 인자 목록으로 만들어서 주생성자 호출
        val instance = primaryConstructor.call(*arguments.toTypedArray())

        // 3. 필드 주입 (필드 클래스에 대해 inject() 재귀 호출)
        clazz.memberProperties.forEach { property: KProperty<*> ->
            if (!property.hasAnnotation<Inject>()) return@forEach
            if (property !is KMutableProperty<*>) return@forEach
            property.isAccessible = true

            val propertyType = property.returnType
            val propertyAnnotation =
                property.annotations.first { it.annotationClass != Inject::class }
            val propertyDependencyKey = DependencyKey(propertyType, propertyAnnotation)

            val subType = typeConverter[propertyDependencyKey] ?: propertyType
            val propertyInstance = inject(subType.jvmErasure)

            cache[propertyDependencyKey] = propertyInstance
            property.setter.call(instance, propertyInstance)
        }

        return instance
    }
}
