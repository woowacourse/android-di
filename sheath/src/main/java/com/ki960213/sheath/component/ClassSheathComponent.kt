package com.ki960213.sheath.component

import com.ki960213.sheath.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible

internal class ClassSheathComponent(
    type: KType,
    name: String,
    isSingleton: Boolean,
    dependentConditions: Map<KType, DependentCondition>,
    private val clazz: KClass<*>,
) : SheathComponent(
    type = type,
    name = name,
    isSingleton = isSingleton,
    dependentConditions = dependentConditions,
) {
    private val cache: MutableMap<KType, SheathComponent> = mutableMapOf()

    override fun instantiate(components: List<SheathComponent>) {
        val dependingComponents = components.filter { this.isDependingOn(it) }

        val constructor = clazz.getInjectConstructor()

        val arguments = constructor.getArgumentsAndSaveInCache(dependingComponents)
        instance = constructor.call(*arguments.toTypedArray())
            ?: throw IllegalArgumentException("인스턴스화 하는 데 필요한 SheathComponent가 없습니다. 정렬 로직을 다시 살펴보세요.")

        instance.injectPropertiesAndSaveInCache(dependingComponents)
        instance.injectFunctionsAndSaveInCache(dependingComponents)
    }

    override fun getNewInstance(): Any {
        val constructor = clazz.getInjectConstructor()

        val arguments = constructor.valueParameters.map { it.getOrCreateInstance() }

        val newInstance = constructor.call(*arguments.toTypedArray())!!

        newInstance.injectNewInstanceAtProperties()
        newInstance.injectNewInstanceAtFunctions()
        return newInstance
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

    private fun Any.injectNewInstanceAtProperties() {
        val properties = findAnnotatedProperties<Inject>()
        properties.forEach { property ->
            if (property !is KMutableProperty1) return@forEach

            val instance = property.getOrCreateInstance()
            property.setter.isAccessible = true
            property.setter.call(this, instance)
        }
    }

    private fun KProperty1<*, *>.getOrCreateInstance(): Any {
        val dependentCondition = dependentConditions[returnType]
            ?: throw IllegalArgumentException("$type 타입의 의존 조건이 없을 수 없습니다. SheathComponentValidator 로직을 다시 살펴보세요.")
        val component = cache[returnType]
            ?: throw IllegalArgumentException("$type 타입의 컴포넌트가 없을 수 없습니다. 컴포넌트 정렬 및 인스턴스화 로직을 다시 살펴보세요.")

        return if (dependentCondition.isNewInstance || !component.isSingleton) {
            component.getNewInstance()
        } else {
            component.instance
        }
    }

    private fun Any.injectNewInstanceAtFunctions() {
        val functions = findAnnotatedFunctions<Inject>()
        functions.forEach { function ->
            val arguments = function.valueParameters.map { it.getOrCreateInstance() }
            function.isAccessible = true
            function.call(this, *arguments.toTypedArray())
        }
    }

    private fun KClass<*>.getInjectConstructor(): KFunction<*> =
        constructors.find { it.hasAnnotation<Inject>() }
            ?: primaryConstructor
            ?: throw IllegalStateException("생성자에 @Inject이 붙지 않고 주 생성자가 없는 클래스는 인스턴스화 할 수 없습니다.")

    private fun KFunction<*>.getArgumentsAndSaveInCache(components: List<SheathComponent>): List<Any> {
        return valueParameters.map { param ->
            val component = components.find { param.type.isSupertypeOf(it.type) }
                ?: throw IllegalArgumentException("${clazz.qualifiedName}의 종속 항목이 존재하지 않아 인스턴스화 할 수 없습니다.")

            cache[param.type] = component

            component.instance
        }
    }

    private fun Any.injectPropertiesAndSaveInCache(components: List<SheathComponent>) {
        val properties = findAnnotatedProperties<Inject>()
        properties.forEach { property -> injectPropertyAndSaveInCache(property, components) }
    }

    private fun Any.injectPropertyAndSaveInCache(
        property: KProperty1<*, *>,
        components: List<SheathComponent>,
    ) {
        val component = components.find { property.returnType.isSupertypeOf(it.type) }
            ?: throw java.lang.IllegalArgumentException("$clazz 클래스의 ${property.name}에 할당할 수 있는 종속 항목이 존재하지 않습니다.")
        if (property is KMutableProperty1) {
            property.setter.isAccessible = true
            property.setter.call(this, component.instance)
        }
        cache[property.returnType] = component
    }

    private fun Any.injectFunctionsAndSaveInCache(components: List<SheathComponent>) {
        val functions = findAnnotatedFunctions<Inject>()
        functions.forEach { function ->
            val arguments = function.getArgumentsAndSaveInCache(components)
            function.isAccessible = true
            function.call(this, *arguments.toTypedArray())
        }
    }

    private inline fun <reified A : Annotation> findAnnotatedProperties(): List<KProperty1<*, *>> =
        clazz.declaredMemberProperties.filter { it.hasAnnotation<A>() }

    private inline fun <reified A : Annotation> findAnnotatedFunctions(): List<KFunction<*>> =
        clazz.declaredMemberFunctions.filter { it.hasAnnotation<A>() }
}
