package com.ki960213.sheath.component

import com.ki960213.sheath.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible

class ClassSheathComponent(
    type: KType,
    name: String,
    isSingleton: Boolean,
    dependentConditions: Map<KType, DependentCondition>,
    private val clazz: KClass<*>,
) : SheathComponent1(
    type = type,
    name = name,
    isSingleton = isSingleton,
    dependentConditions = dependentConditions,
) {
    private val cache: MutableMap<KType, SheathComponent1> = mutableMapOf()

    override fun instantiate(components: List<SheathComponent1>) {
        val constructor = clazz.getInjectConstructor()

        instance = constructor.call(*constructor.getArguments(components).toTypedArray()) ?: throw IllegalArgumentException("인스턴스화 하는 데 필요한 SheathComponent가 없습니다. 정렬 로직을 다시 살펴보세요.")

        instance.injectProperty(components)
        instance.injectFunction(components)
    }

    override fun getNewInstance(): Any {
        val constructor = clazz.getInjectConstructor()

        val constructorArgs = constructor.valueParameters.map { param ->
            if (dependentConditions[param.type]!!.isNewInstance) {
                cache[param.type]!!.getNewInstance()
            } else {
                if (cache[param.type]!!.isSingleton) {
                    cache[param.type]!!.instance
                } else {
                    cache[param.type]!!.getNewInstance()
                }
            }
        }

        val newInstance = constructor.call(*constructorArgs.toTypedArray())!!

        newInstance.injectNewInstanceAtProperties()
        newInstance.injectNewInstanceAtFunctions()
        return newInstance
    }

    private fun Any.injectNewInstanceAtProperties() {
        val properties = findAnnotatedProperties<Inject>()
        properties.forEach { property ->
            val instance = if (dependentConditions[property.returnType]!!.isNewInstance) {
                cache[property.returnType]!!.getNewInstance()
            } else {
                if (cache[property.returnType]!!.isSingleton) {
                    cache[property.returnType]!!.instance
                } else {
                    cache[property.returnType]!!.getNewInstance()
                }
            }
            if (property is KMutableProperty1) {
                property.setter.isAccessible = true
                property.setter.call(this, instance)
            }
        }
    }

    private fun Any.injectNewInstanceAtFunctions() {
        val functions = findAnnotatedFunctions<Inject>()
        functions.forEach { function ->
            val arguments = function.valueParameters.map { param ->
                if (dependentConditions[param.type]!!.isNewInstance) {
                    cache[param.type]!!.getNewInstance()
                } else {
                    if (cache[param.type]!!.isSingleton) {
                        cache[param.type]!!.instance
                    } else {
                        cache[param.type]!!.getNewInstance()
                    }
                }
            }
            function.isAccessible = true
            function.call(this, *arguments.toTypedArray())
        }
    }

    private fun KClass<*>.getInjectConstructor(): KFunction<*> =
        constructors.find { it.hasAnnotation<Inject>() }
            ?: primaryConstructor
            ?: throw IllegalStateException("생성자에 @Inject이 붙지 않고 주 생성자가 없는 클래스는 인스턴스화 할 수 없습니다.")

    private fun KFunction<*>.getArguments(components: List<SheathComponent1>): List<Any> {
        return valueParameters.map { param ->
            val component = components.find { param.type.isSupertypeOf(it.type) }
                ?: throw IllegalArgumentException("${clazz.qualifiedName}의 종속 항목이 존재하지 않아 인스턴스화 할 수 없습니다.")

            cache[param.type] = component

            component.instance
        }
    }

    private fun Any.injectProperty(components: List<SheathComponent1>) {
        val properties = findAnnotatedProperties<Inject>()
        properties.forEach { property ->
            val component = components.find { property.returnType.isSupertypeOf(it.type) }
                ?: throw java.lang.IllegalArgumentException("$clazz 클래스의 ${property.name}에 할당할 수 있는 종속 항목이 존재하지 않습니다.")
            if (property is KMutableProperty1) {
                property.setter.isAccessible = true
                property.setter.call(this, component.instance)
            }
            cache[property.returnType] = component
        }
    }

    private fun Any.injectFunction(components: List<SheathComponent1>) {
        val functions = findAnnotatedFunctions<Inject>()
        functions.forEach { function ->
            val arguments = function.getArguments(components)
            function.isAccessible = true
            function.call(this, *arguments.toTypedArray())
        }
    }

    private inline fun <reified A : Annotation> findAnnotatedProperties(): List<KProperty1<*, *>> =
        clazz.declaredMemberProperties.filter { it.hasAnnotation<A>() }

    private inline fun <reified A : Annotation> findAnnotatedFunctions(): List<KFunction<*>> =
        clazz.declaredMemberFunctions.filter { it.hasAnnotation<A>() }
}
