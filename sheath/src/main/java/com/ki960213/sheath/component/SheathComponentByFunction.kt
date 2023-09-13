package com.ki960213.sheath.component

import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Module
import com.ki960213.sheath.annotation.Prototype
import com.ki960213.sheath.annotation.Qualifier
import com.ki960213.sheath.extention.customQualifiedName
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaMethod

class SheathComponentByFunction(private val function: KFunction<*>) : SheathComponent() {
    override val clazz: KClass<*> = function.returnType.classifier as KClass<*>

    override val name: String = function.customQualifiedName ?: clazz.qualifiedName
        ?: throw IllegalArgumentException("전역적인 클래스로만 SheathComponent를 생성할 수 있습니다.")

    override val isSingleton: Boolean = !function.hasAnnotation<Prototype>()

    private val dependingConditions: Map<KClass<*>, DependingCondition> =
        function.valueParameters.associate { param ->
            param.type.classifier as KClass<*> to DependingCondition.from(param)
        }

    override val dependentCount: Int = function.valueParameters.size

    init {
        validateModuleObject()
        validateModuleAnnotation()
        validateComponentAnnotation()
        validateReturnType()
        validateQualifier()
    }

    override fun isDependingOn(component: SheathComponent): Boolean {
        val dependingClass = function.valueParameters.find { param ->
            val clazz = param.type.classifier as KClass<*>
            clazz.isSuperclassOf(component.clazz)
        }?.type?.classifier as? KClass<*> ?: return false

        val qualifiedName = dependingConditions[dependingClass]!!.qualifiedName ?: return true
        return qualifiedName == component.name
    }

    override fun instantiated(instances: List<Any>): Any {
        val obj = function.javaMethod?.declaringClass?.kotlin?.objectInstance
        return function.call(obj, *function.getArguments(instances).toTypedArray())
            ?: throw IllegalArgumentException("null을 생성하는 함수는 SheathComponent가 될 수 없습니다.")
    }

    private fun validateModuleObject() {
        requireNotNull(function.javaMethod?.declaringClass?.kotlin?.objectInstance) {
            "${function.name} 함수가 정의된 클래스가 object가 아닙니다."
        }
    }

    private fun validateModuleAnnotation() {
        requireNotNull(function.javaMethod?.declaringClass?.getAnnotation(Module::class.java)) {
            "${function.name} 함수가 정의된 클래스에 @Module이 붙어있어야 합니다."
        }
    }

    private fun validateComponentAnnotation() {
        require(function.hasAnnotation<Component>()) { "${function.name} 함수에 @Component가 붙어있지 않습니다." }
    }

    private fun validateReturnType() {
        require(!function.returnType.isMarkedNullable) { "SheathComponent를 생성할 함수의 리턴 타입이 nullable이면 안됩니다" }
    }

    private fun validateQualifier() {
        require(function.isNotDuplicatedQualifier() && function.valueParameters.all { it.isNotDuplicatedQualifier() }) { "여러 개의 한정자 애노테이션을 붙일 수 없습니다." }
    }

    private fun KAnnotatedElement.isNotDuplicatedQualifier(): Boolean =
        annotations.count { it.annotationClass == Qualifier::class || it.annotationClass.hasAnnotation<Qualifier>() } <= 1

    private fun KFunction<*>.getArguments(instances: List<Any>): List<Any> =
        valueParameters.map { param ->
            instances.find { (param.type.classifier as KClass<*>).isInstance(it) }
                ?: throw IllegalArgumentException("${this.name} 함수의 인자로 넣어줄 종속 항목이 존재하지 않습니다.")
        }
}
