package com.example.di

import android.content.Context
import com.example.di.annotations.ActivityContext
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

typealias Declaration<T> = () -> T

data class Dependency(
    private val module: AppModule,
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
        if (declaration != null) {
            return declaration!!.invoke()
                ?: throw IllegalStateException("return value of function is null")
        }
        val paramsInstances = getParamsInstances()
        declaration = { function.callBy(paramsInstances) }
        savedInstance = declaration?.invoke()
        return savedInstance ?: throw java.lang.IllegalStateException("return value of function is null")
    }

    private fun getParamsInstances() =
        params.associateWith {
            if (it.hasAnnotation<ActivityContext>()) return@associateWith context
            if (it.type.jvmErasure.isSubclassOf(AppModule::class)) return@associateWith module
            DIContainer.getDependencyInstance(
                it.type.jvmErasure,
                it.getQualifierAnnotation(),
            )
        }
}
