package com.ki960213.sheath.component

import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaMethod

internal class FunctionSheathComponent(
    type: KType,
    name: String,
    isSingleton: Boolean,
    dependentConditions: Map<KType, DependentCondition>,
    private val function: KFunction<*>,
) : SheathComponent(
    type = type,
    name = name,
    isSingleton = isSingleton,
    dependentConditions = dependentConditions,
) {
    private val cache: MutableMap<KType, SheathComponent> = mutableMapOf()

    override fun instantiate(components: List<SheathComponent>) {
        val dependingComponents = components.filter { this.isDependingOn(it) }

        val obj = function.javaMethod?.declaringClass?.kotlin?.objectInstance
        val arguments = function.getArgumentsAndSaveInCache(dependingComponents)
        instance = function.call(obj, *arguments.toTypedArray())
            ?: throw IllegalArgumentException("null을 생성하는 함수는 SheathComponent가 될 수 없습니다.")
    }

    override fun getNewInstance(): Any {
        val obj = function.javaMethod?.declaringClass?.kotlin?.objectInstance

        val arguments = function.valueParameters.map { it.getOrCreateInstance() }

        return function.call(obj, *arguments.toTypedArray())
            ?: throw IllegalArgumentException("null을 생성하는 함수는 SheathComponent가 될 수 없습니다.")
    }

    private fun KParameter.getOrCreateInstance(): Any {
        val dependentCondition = dependentConditions[type]
            ?: throw IllegalArgumentException("$type 타입의 의존 조건이 없을 수 없습니다. SheathComponentValidator 로직을 다시 살펴보세요.")
        val component = cache[type]
            ?: throw IllegalArgumentException("$type 타입의 컴포넌트가 없을 수 없습니다. 컴포넌트 정렬 및 인스턴스화 로직을 다시 살펴보세요.")

        return if (dependentCondition.isNewInstance || !component.isSingleton) {
            component.getNewInstance()
        } else {
            component.instance
        }
    }

    private fun KFunction<*>.getArgumentsAndSaveInCache(components: List<SheathComponent>): List<Any> =
        valueParameters.map { param ->
            val component = components.find { param.type.isSupertypeOf(it.type) }
                ?: throw IllegalArgumentException("$name 함수의 종속 항목이 존재하지 않아 인스턴스화 할 수 없습니다.")

            cache[param.type] = component

            component.instance
        }
}
