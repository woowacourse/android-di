package com.ki960213.sheath.component

import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Inject
import com.ki960213.sheath.annotation.Prototype
import com.ki960213.sheath.extention.hasAnnotationOrHasAttachedAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible

internal class ClassSheathComponent(
    private val clazz: KClass<*>,
) : SheathComponent() {

    override val type: KType = clazz.createType()

    override val name: String = clazz.qualifiedName
        ?: throw IllegalArgumentException("전역적인 클래스로만 SheathComponent를 생성할 수 있습니다.")

    override val isSingleton: Boolean = !clazz.hasAnnotationOrHasAttachedAnnotation<Prototype>()

    override val dependentConditions: Map<KType, DependentCondition> =
        clazz.extractFromConstructor() + clazz.extractFromProperties() + clazz.extractFromFunctions()

    private val cache: MutableMap<KType, SheathComponent> = mutableMapOf()

    init {
        clazz.validateComponentAnnotation()
        clazz.validateConstructorInjection()
        clazz.validateDuplicateDependingType()
    }

    override fun instantiate(components: List<SheathComponent>) {
        val dependingComponents = components.filter { this.isDependingOn(it) }

        val constructor = clazz.getInjectConstructor()

        val arguments = constructor.getArgumentsAndSaveInCache(dependingComponents)
        instance = constructor.call(*arguments.toTypedArray())!!

        instance.injectPropertiesAndSaveInCache(dependingComponents)
        instance.injectFunctionsAndSaveInCache(dependingComponents)
    }

    override fun getNewInstance(): Any {
        val constructor = clazz.getInjectConstructor()

        val arguments = constructor.valueParameters.map { getOrCreateInstanceOf(it.type) }

        val newInstance = constructor.call(*arguments.toTypedArray())!!

        newInstance.injectNewInstanceAtProperties()
        newInstance.injectNewInstanceAtFunctions()
        return newInstance
    }

    private fun KClass<*>.validateComponentAnnotation() {
        require(hasAnnotationOrHasAttachedAnnotation<Component>()) {
            "클래스에 @Component 혹은 @Component가 붙은 애노테이션이 붙어 있지 않다면 SheathComponent를 생성할 수 없습니다."
        }
    }

    private fun KClass<*>.validateConstructorInjection() {
        require(constructors.count { it.hasAnnotation<Inject>() } <= 1) {
            "여러 개의 생성자에 @Inject 애노테이션을 붙일 수 없습니다."
        }
    }

    private fun KClass<*>.validateDuplicateDependingType() {
        require(getDependingTypes() == getDependingTypes().distinct()) {
            "${this.qualifiedName} 클래스는 같은 타입을 여러 곳에서 의존하고 있습니다."
        }
    }

    private fun KClass<*>.getDependingTypes(): List<KType> =
        getConstructorInjectionDependingTypes() +
            getPropertyInjectionDependingTypes() +
            getFunctionInjectionDependingTypes()

    private fun KClass<*>.getConstructorInjectionDependingTypes(): List<KType> =
        (constructors.find { it.hasAnnotation<Inject>() } ?: primaryConstructor)
            ?.valueParameters
            ?.map { it.type }
            ?: emptyList()

    private fun KClass<*>.getPropertyInjectionDependingTypes(): List<KType> =
        declaredMemberProperties
            .filter { it.hasAnnotation<Inject>() }
            .map { it.returnType }

    private fun KClass<*>.getFunctionInjectionDependingTypes(): List<KType> =
        declaredMemberFunctions
            .filter { it.hasAnnotation<Inject>() }
            .flatMap { func -> func.valueParameters.map { it.type } }

    private fun KClass<*>.extractFromConstructor(): Map<KType, DependentCondition> =
        constructors.find { it.hasAnnotation<Inject>() }
            ?.valueParameters
            ?.associate { it.type to DependentCondition.from(it) }
            ?: extractFromPrimaryConstructor()

    private fun KClass<*>.extractFromPrimaryConstructor(): Map<KType, DependentCondition> =
        primaryConstructor?.valueParameters
            ?.associate { it.type to DependentCondition.from(it) }
            ?: mapOf()

    private fun KClass<*>.extractFromProperties(): Map<KType, DependentCondition> =
        declaredMemberProperties.filter { it.hasAnnotation<Inject>() }
            .associate { it.returnType to DependentCondition.from(it) }

    private fun KClass<*>.extractFromFunctions(): Map<KType, DependentCondition> =
        declaredMemberFunctions.filter { it.hasAnnotation<Inject>() }
            .flatMap { function ->
                function.valueParameters.map { it.type to DependentCondition.from(it) }
            }
            .toMap()

    private fun Any.injectNewInstanceAtProperties() {
        val properties = findAnnotatedProperties<Inject>()
        properties.forEach { property ->
            if (property !is KMutableProperty1) return@forEach

            val instance = getOrCreateInstanceOf(property.returnType)
            property.setter.isAccessible = true
            property.setter.call(this, instance)
        }
    }

    private fun getOrCreateInstanceOf(type: KType): Any {
        val dependentCondition = dependentConditions[type]
            ?: throw AssertionError("$type 타입의 의존 조건이 없을 수 없습니다. 의존 조건 초기화 로직을 다시 살펴보세요.")
        val component = cache[type]
            ?: throw AssertionError("$type 타입의 컴포넌트가 없을 수 없습니다. 컴포넌트 정렬 및 인스턴스화 로직을 다시 살펴보세요.")

        return if (dependentCondition.isNewInstance || !component.isSingleton) {
            component.getNewInstance()
        } else {
            component.instance
        }
    }

    private fun Any.injectNewInstanceAtFunctions() {
        val functions = findAnnotatedFunctions<Inject>()
        functions.forEach { function ->
            val arguments = function.valueParameters.map { getOrCreateInstanceOf(it.type) }
            function.isAccessible = true
            function.call(this, *arguments.toTypedArray())
        }
    }

    private fun KClass<*>.getInjectConstructor(): KFunction<*> =
        constructors.find { it.hasAnnotation<Inject>() }
            ?: primaryConstructor
            ?: throw IllegalArgumentException("생성자에 @Inject이 붙지 않고 주 생성자가 없는 클래스는 인스턴스화 할 수 없습니다.")

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
            ?: throw IllegalArgumentException("$clazz 클래스의 ${property.name}에 할당할 수 있는 종속 항목이 존재하지 않습니다.")
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
