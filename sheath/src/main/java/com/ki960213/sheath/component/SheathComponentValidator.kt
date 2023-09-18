package com.ki960213.sheath.component

import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Inject
import com.ki960213.sheath.annotation.Module
import com.ki960213.sheath.extention.getDependingTypes
import com.ki960213.sheath.extention.hasAnnotationOrHasAttachedAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.javaMethod

internal object SheathComponentValidator {

    fun validate(clazz: KClass<*>) {
        clazz.validateComponentAnnotation()
        clazz.validateConstructorInjection()
        clazz.validateDuplicateDependingType()
    }

    fun validate(function: KFunction<*>) {
        function.validateComponentAnnotation()
        function.validateModuleIsObject()
        function.validateModuleAnnotation()
        function.validateReturnType()
        function.validateDuplicateDependingType()
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
}
