package com.woosuk.scott_di

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

// DI 시작
fun startDI(block: DiContainer.() -> Unit) = DiContainer.apply(block)

// get 은 DI Container에 존재하는 객체 그대로 주입할 때 사용
// Inject는 DI Container에 없기 떄문에, 객체의 생성자를 구하고, DI Container에 있는 객체들을 이용하여 객체를 생성할 때 사용.

// 컴파일 타임에서 인자의 정보를 알 수 있을 때, 객체 가져오기
inline fun <reified T : Any> get(): T {
    val kClazz = T::class
    return when {
        DiContainer.singletons.keys.contains(kClazz) -> DiContainer.singletons[kClazz] as T
        DiContainer.declarations.keys.contains(kClazz) -> DiContainer.declarations[kClazz]?.invoke() as T
        else -> throw IllegalArgumentException("$kClazz 의존성 주입할 객체가 없습니다.")
    }
}

// 컴파일 타입에서 인자의 정보를 알 수 없을 때, KClass 를 통해 주입한 객체들 가져오기
fun get(kClazz: KClass<*>): Any {
    return when {
        DiContainer.singletons.keys.contains(kClazz) -> DiContainer.singletons[kClazz]!!
        DiContainer.declarations.keys.contains(kClazz) -> DiContainer.declarations[kClazz]?.invoke()!!
        else -> throw IllegalArgumentException("KClass $kClazz 의존성 주입할 객체가 없습니다.")
    }
}

// Quliified 로 주입한 객체들 가져오기
fun get(qualifierName: String): Any {
    return when {
        DiContainer.qualifiedSingletons.keys.contains(qualifierName)
        -> DiContainer.qualifiedSingletons[qualifierName]!!

        DiContainer.qualifiedDeclarations.keys.contains(qualifierName)
        -> DiContainer.qualifiedDeclarations[qualifierName]?.invoke()!!

        else -> throw IllegalArgumentException("Qulifeier $qualifierName 의존성 주입할 객체가 없습니다.")
    }
}

// 생성자 주입 + 필드 주입
inline fun <reified T : Any> inject(): T {
    val instance = initWithDependencies<T>().apply {
        injectFields(this)
    }
    return instance
}

// 생성자 주입
inline fun <reified T : Any> initWithDependencies(): T {
    val primaryConstructor =
        T::class.primaryConstructor ?: throw IllegalStateException("주 생성자가 없어요...ㅠ")

    val constructorParameters = primaryConstructor.parameters

    val instances = constructorParameters.map {
        if (it.hasAnnotation<Qualifier>()) get(it.findAnnotation<Qualifier>()!!.name)
        else get(it.type.jvmErasure)
    }.toTypedArray()
    return primaryConstructor.call(*instances)
}

// 필드 주입
inline fun <reified T : Any> injectFields(instance: T) {
    val properties = instance::class
        .declaredMemberProperties.filter { it.hasAnnotation<Inject>() }

    properties.forEach { property ->
        property.isAccessible = true
        val value = get(property.returnType.jvmErasure)
        property.javaField?.set(instance, value)
    }
}

