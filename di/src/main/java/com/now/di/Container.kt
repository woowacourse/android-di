package com.now.di

import com.now.annotation.Qualifier
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

class Container {
    private val dependency = mutableMapOf<DependencyType, Any>()

    fun getInstance(dependencyType: DependencyType): Any? {
        return dependency[dependencyType]
    }

    fun addInstance(receiver: Module, kFunction: KFunction<*>) {
        val dependencyType = getDependencyTypeFromKFunction(kFunction)

        // dependency에 이미 있다면 추가할 필요가 없기 때문에 종료
        if (getInstance(dependencyType) != null) return

        // dependency에 없는 경우
        if (kFunction.valueParameters.isEmpty()) {
            // provide 함수에 파라미터가 없는 경우 인스턴스를 생성하고 dependency에 추가 후 종료
            kFunction.call(receiver)?.let { addDependency(dependencyType, it) }
                ?: throw IllegalArgumentException("문제 생김")
            return
        }

        // provide 함수에 파라미터가 있는 경우 dependency에서 가져오거나 생성해야 함
        val requiredParams = kFunction.valueParameters.map { kParameter ->
            val klass = kParameter.type.jvmErasure
            val annotation = kParameter.findAnnotation<Qualifier>()
            getInstance(DependencyType(klass, annotation)) ?: createParameter(receiver, kParameter)
        }

        // 인스턴스를 생성하여 dependency에 저장
        kFunction.call(receiver, *requiredParams.toTypedArray())?.let {
            addDependency(dependencyType, it)
        }
    }

    private fun getDependencyTypeFromKFunction(kFunction: KFunction<*>): DependencyType {
        return DependencyType(
            kFunction.returnType.jvmErasure,
            kFunction.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() },
        )
    }

    private fun createParameter(receiver: Module, kParameter: KParameter): Any {
        val kFunction = receiver::class.declaredFunctions.firstOrNull { kFunction ->
            kFunction.returnType == kParameter.type
        } ?: throw NullPointerException("생성할 수 없습니다!")
        addInstance(receiver, kFunction)
        // 재귀적 호출 이후라 무조건 있음
        return getInstance(DependencyType(kParameter.type.jvmErasure, kParameter.findAnnotation<Qualifier>()))!!
    }

    private fun addDependency(dependencyType: DependencyType, instance: Any) {
        dependency[dependencyType] = instance
    }
}
