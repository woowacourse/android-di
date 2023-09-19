package com.hyegyeong.di

import com.hyegyeong.di.annotations.Qualifier
import com.hyegyeong.di.annotations.Singleton
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

class DiContainer(private val dependencyModule: DiModule) {

    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()
    fun provideInstance(kClass: KClass<*>, annotation: List<Annotation>): Any {
        val containerFunctions: Collection<KFunction<*>> = dependencyModule::class.declaredFunctions
        val function: KFunction<*> = containerFunctions.first {
            (it.returnType.jvmErasure == kClass) && it.annotations.containsAll(annotation)
        }
        val instance = createInstance(function)
        if (function.annotations.contains(Singleton())) {
            val value = instances[kClass]
            if (value != null) return value
            instances[kClass] = instance
        }
        return instance
    }

    private fun createInstance(function: KFunction<*>): Any {
        val instances = mutableListOf<Any>()
        if (function.valueParameters.isNotEmpty()) {
            function.valueParameters.forEach {
                instances.add(
                    provideInstance(
                        it.type.jvmErasure,
                        it.annotations.filter { annotation -> annotation.annotationClass.hasAnnotation<Qualifier>() }
                    )
                )
            }
        }
        return function.call(dependencyModule, * instances.toTypedArray())
            ?: throw IllegalArgumentException("해당 함수를 찾을 수 없습니다.")
    }
}