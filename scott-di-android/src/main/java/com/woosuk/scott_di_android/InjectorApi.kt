package com.woosuk.scott_di_android

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

inline fun <reified T : ViewModel> injectViewModel(): T {
    val injectableTypes =
        T::class.declaredMemberProperties
            .filter { it.hasAnnotation<Inject>() }
            .map { it.returnType }

    val dependencies = DiApplication.module::class.declaredMemberFunctions
        .filter { it.hasAnnotation<ViewModelScope>() }
        .filter { injectableTypes.contains(it.returnType) }
        .map { Dependency(DiApplication.module, it) }

    dependencies.forEach { DiContainer.addDependency(it) }
    return injectConstructor(T::class)
}

fun <T : Any> injectConstructor(kClazz: KClass<T>): T {
    val primaryConstructor =
        kClazz.primaryConstructor
            ?: throw IllegalStateException("${kClazz.simpleName} 주 생성자가 없어요...ㅠ")

    val constructorParameters = primaryConstructor.parameters

    val instances = constructorParameters.map { parameter ->
        DiContainer.getDependencyInstance(
            parameter.type.jvmErasure,
            parameter.getQualifierAnnotation(),
        )
    }.toTypedArray()
    return primaryConstructor.call(*instances)
}
