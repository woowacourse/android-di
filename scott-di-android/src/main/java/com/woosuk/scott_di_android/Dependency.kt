package com.woosuk.scott_di_android

import android.content.Context
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

typealias Declaration<T> = () -> T

data class Dependency(
    private val module: Module,
    private val function: KFunction<*>,
    val context: Context? = null,
) {
    val qualifierAnnotation: Annotation? = function.getQualifierAnnotation()

    val typeClass: KClass<*> = function.returnType.jvmErasure

    private var declaration: Declaration<*>? = null

    private var savedInstance: Any? = null

    private val params = function.parameters

    fun getInstance(): Any {
        if (savedInstance != null) return savedInstance!!
        if (declaration != null) return declaration!!.invoke()
            ?: throw IllegalStateException("함수 반환값이 null입니다")
        val paramsInstances = getParamsInstances()
        declaration = { function.callBy(paramsInstances) }
        savedInstance = declaration?.invoke()
        return savedInstance ?: throw java.lang.IllegalStateException("함수의 반환 값이 null 입니다")
    }

    // 함수의 파라미터를 DI 로 넣어주기
    private fun getParamsInstances() =
        params.associateWith {
            if (it.hasAnnotation<ActivityContext>()) return@associateWith context
            if (it.type.jvmErasure.isSubclassOf(Module::class)) return@associateWith module
            DiContainer.getDependencyInstance(
                it.type.jvmErasure,
                it.getQualifierAnnotation(),
            )
        }
}
