package com.ki960213.sheath.component

import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Module
import com.ki960213.sheath.annotation.Prototype
import com.ki960213.sheath.extention.hasAnnotationOrHasAttachedAnnotation
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.jvmErasure

internal class FunctionSheathComponent(
    private val function: KFunction<*>,
) : SheathComponent() {

    override val type: KType = function.returnType

    override val name: String = function.returnType.jvmErasure.qualifiedName
        ?: throw IllegalArgumentException("전역적인 클래스로만 SheathComponent를 생성할 수 있습니다.")

    override val isSingleton: Boolean = !function.hasAnnotationOrHasAttachedAnnotation<Prototype>()

    override val dependentConditions: Map<KType, DependentCondition> = function.valueParameters
        .associate { it.type to DependentCondition.from(it) }

    private val cache: MutableMap<KType, SheathComponent> = mutableMapOf()

    init {
        function.validateComponentAnnotation()
        function.validateModuleIsObject()
        function.validateModuleAnnotation()
        function.validateReturnType()
        function.validateDuplicateDependingType()
    }

    override fun instantiate(components: List<SheathComponent>) {
        val dependingComponents = components.filter { this.isDependingOn(it) }

        val arguments = function.getArgumentsAndSaveInCache(dependingComponents)
        instance = createInstance(arguments)
    }

    override fun getNewInstance(): Any {
        val arguments = function.valueParameters.map { it.getOrCreateInstance() }

        return createInstance(arguments)
    }

    private fun createInstance(arguments: List<Any>): Any {
        val obj = function.javaMethod?.declaringClass?.kotlin?.objectInstance

        return function.call(obj, *arguments.toTypedArray())
            ?: throw AssertionError("nullable 타입을 반환하는 함수로 FunctionSheathComponent 객체를 생성할 수 없게 했습니다.")
    }

    private fun KFunction<*>.validateComponentAnnotation() {
        require(hasAnnotationOrHasAttachedAnnotation<Component>()) {
            "함수에 @Component 혹은 @Component가 붙은 애노테이션이 붙어 있지 않다면 SheathComponent를 생성할 수 없습니다."
        }
    }

    private fun KFunction<*>.validateModuleIsObject() {
        requireNotNull(javaMethod?.declaringClass?.kotlin?.objectInstance) {
            "${this.name} 함수가 정의된 클래스가 object가 아닙니다."
        }
    }

    private fun KFunction<*>.validateModuleAnnotation() {
        requireNotNull(javaMethod?.declaringClass?.getAnnotation(Module::class.java)) {
            "${this.name} 함수가 정의된 클래스에 @Module이 붙어있어야 합니다."
        }
    }

    private fun KFunction<*>.validateReturnType() {
        require(!returnType.isMarkedNullable) {
            "SheathComponent를 생성할 함수의 리턴 타입이 nullable이면 안됩니다"
        }
    }

    private fun KFunction<*>.validateDuplicateDependingType() {
        require(getDependingTypes() == getDependingTypes().distinct()) {
            "${this.name} 함수는 같은 타입을 여러 매개 변수로 가지고 있습니다."
        }
    }

    private fun KFunction<*>.getDependingTypes(): List<KType> = valueParameters.map { it.type }

    private fun KParameter.getOrCreateInstance(): Any {
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

    private fun KFunction<*>.getArgumentsAndSaveInCache(components: List<SheathComponent>): List<Any> =
        valueParameters.map { param ->
            val component = components.find { param.type.isSupertypeOf(it.type) }
                ?: throw IllegalArgumentException("$name 함수의 종속 항목이 존재하지 않아 인스턴스화 할 수 없습니다.")

            cache[param.type] = component

            component.instance
        }
}
