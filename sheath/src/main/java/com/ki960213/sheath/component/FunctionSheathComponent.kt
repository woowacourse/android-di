package com.ki960213.sheath.component

import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaMethod

class FunctionSheathComponent(
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
        instance = function.call(*function.getArguments(dependingComponents).toTypedArray())
            ?: throw IllegalArgumentException("null을 생성하는 함수는 SheathComponent가 될 수 없습니다.")
    }

    override fun getNewInstance(): Any {
        val obj = function.javaMethod?.declaringClass?.kotlin?.objectInstance
        val arguments = function.valueParameters.map { param ->
            if (dependentConditions[param.type]!!.isNewInstance) {
                cache[param.type]?.getNewInstance()
            } else {
                if (cache[param.type]!!.isSingleton) {
                    cache[param.type]!!.instance
                } else {
                    cache[param.type]!!.getNewInstance()
                }
            }
        }
        return function.call(*arguments.toTypedArray())!!
    }

    private fun KFunction<*>.getArguments(components: List<SheathComponent>): List<Any> {
        return valueParameters.map { param ->
            val component = components.find { param.type.isSupertypeOf(it.type) }
                ?: throw IllegalArgumentException("$name 함수의 종속 항목이 존재하지 않아 인스턴스화 할 수 없습니다.")

            cache[param.type] = component

            component.instance
        }
    }
}
