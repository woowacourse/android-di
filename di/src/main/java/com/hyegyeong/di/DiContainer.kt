package com.hyegyeong.di

import com.hyegyeong.di.annotations.Inject
import com.hyegyeong.di.annotations.Qualifier
import com.hyegyeong.di.annotations.Singleton
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

object DiContainer {
    // TODO: DiContainer 의 모듈이 액티비티마다의 모듈로 갈아끼워지도록 수정
    lateinit var appModule: DiModule
    lateinit var dependencyModule: DiModule
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()
    fun provideInstance(kClass: KClass<*>, annotations: List<Annotation>): Any {
        var module = dependencyModule
        if (annotations.contains(Singleton())) module = appModule
        val containerFunctions: Collection<KFunction<*>> = module::class.declaredFunctions
        val function: KFunction<*> = containerFunctions.first {
            (it.returnType.jvmErasure == kClass) && it.annotations.containsAll(annotations.filterNot { annotation -> annotation == Inject() })
        }

        val instance = createInstance(function, module)
        if (function.annotations.contains(Singleton())) {
            val value = instances[kClass]
            if (value != null) return value
            instances[kClass] = instance
        }
        return instance
    }

    private fun createInstance(function: KFunction<*>, module: DiModule): Any {
        val instances = mutableListOf<Any>()
        if (function.valueParameters.isNotEmpty()) {
            function.valueParameters.forEach {
                if (it.hasAnnotation<Inject>())
                    instances.add(
                        provideInstance(
                            it.type.jvmErasure,
                            it.annotations.filter { annotation -> annotation.annotationClass.hasAnnotation<Qualifier>() }
                        )
                    )
            }
        }
        println("")
        return function.call(module, * instances.toTypedArray())
            ?: throw IllegalArgumentException("해당 함수를 찾을 수 없습니다.")
    }
}
