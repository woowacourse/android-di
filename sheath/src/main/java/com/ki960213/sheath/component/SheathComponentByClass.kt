package com.ki960213.sheath.component

import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Inject
import com.ki960213.sheath.annotation.Prototype
import com.ki960213.sheath.annotation.Qualifier
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible

class SheathComponentByClass(override val clazz: KClass<*>) : SheathComponent() {

    override val name: String = clazz.getQualifiedName() ?: clazz.qualifiedName
        ?: throw IllegalArgumentException("전역적인 클래스로만 SheathComponent를 생성할 수 있습니다.")

    override val isSingleton: Boolean = !clazz.hasAnnotation<Prototype>()

    private val dependingConditions: Map<KClass<*>, DependingCondition> = (
        (
            findAnnotatedConstructor<Inject>()?.valueParameters?.associate {
                it.type.classifier as KClass<*> to DependingCondition.from(it)
            } ?: mapOf()
            ) +
            findAnnotatedProperties<Inject>().associate {
                it.returnType.classifier as KClass<*> to DependingCondition.from(it)
            } +
            findAnnotatedFunctions<Inject>().flatMap { function ->
                function.valueParameters.map {
                    it.type.classifier as KClass<*> to DependingCondition.from(it)
                }
            }.toMap()
        ).takeUnless { it.isEmpty() }
        ?: clazz.primaryConstructor?.valueParameters?.associate {
            it.type.classifier as KClass<*> to DependingCondition.from(it)
        } ?: mapOf()

    override val dependentCount: Int = dependingConditions.count()

    init {
        validateComponentAnnotation()
        validateQualifier()
        validateConstructorInjection()
    }

    override fun isDependingOn(component: SheathComponent): Boolean {
        val dependingClass =
            dependingConditions.keys.find { it.isSuperclassOf(component.clazz) } ?: return false
        val qualifiedName = dependingConditions[dependingClass]!!.qualifiedName ?: return true
        return qualifiedName == component.name
    }

    override fun instantiated(instances: List<Any>): Any {
        val instance = createInstanceByConstructor(instances)

        instance.injectProperty(instances)
        instance.injectFunction(instances)

        return instance
    }

    private fun validateComponentAnnotation() {
        require(clazz.hasAnnotation<Component>() || clazz.annotations.any { annotation -> annotation.annotationClass.hasAnnotation<Component>() }) {
            "@Component가 붙은 클래스 혹은 @Component가 붙은 애노테이션이 붙은 클래스로만 SheathComponent를 생성할 수 있습니다."
        }
    }

    private fun validateConstructorInjection() {
        require(clazz.constructors.count { it.hasAnnotation<Inject>() } <= 1) {
            "여러 개의 생성자에 @Inject 애노테이션을 붙일 수 없습니다."
        }
    }

    private fun validateQualifier() {
        require(
            clazz.isNotDuplicatedQualifier() &&
                (findAnnotatedConstructor<Inject>()?.isNotDuplicatedQualifier() ?: true) &&
                findAnnotatedProperties<Inject>().all { it.isNotDuplicatedQualifier() } &&
                findAnnotatedFunctions<Inject>().all { it.isNotDuplicatedQualifier() },
        ) {
            "여러 개의 한정자 애노테이션을 붙일 수 없습니다."
        }
    }

    private fun KAnnotatedElement.isNotDuplicatedQualifier(): Boolean =
        annotations.count { it.annotationClass == Qualifier::class || it.annotationClass.hasAnnotation<Qualifier>() } <= 1

    private inline fun <reified A : Annotation> findAnnotatedConstructor(): KFunction<Any>? =
        clazz.constructors.find { it.hasAnnotation<A>() }

    private inline fun <reified A : Annotation> findAnnotatedProperties(): List<KProperty1<*, *>> =
        clazz.declaredMemberProperties.filter { it.hasAnnotation<A>() }

    private inline fun <reified A : Annotation> findAnnotatedFunctions(): List<KFunction<*>> =
        clazz.declaredMemberFunctions.filter { it.hasAnnotation<A>() }

    private fun createInstanceByConstructor(instances: List<Any>): Any {
        val constructor = findAnnotatedConstructor<Inject>() ?: clazz.primaryConstructor
            ?: throw IllegalArgumentException("주 생성자가 없는 클래스는 인스턴스화 할 수 없습니다.")
        val args = constructor.getArguments(instances)
        return constructor.call(*args.toTypedArray())
    }

    private fun KFunction<*>.getArguments(instances: List<Any>): List<Any> =
        valueParameters.map { param ->
            instances.find { (param.type.classifier as KClass<*>).isInstance(it) }
                ?: throw IllegalArgumentException("$clazz 클래스의 ${this.name} 함수의 인자로 넣어줄 종속 항목이 존재하지 않습니다.")
        }

    private fun Any.injectProperty(instances: List<Any>) {
        val properties = findAnnotatedProperties<Inject>()
        properties.forEach { property ->
            val argument = instances.find {
                (property.returnType.classifier as KClass<*>).isInstance(it)
            }
                ?: throw java.lang.IllegalArgumentException("$clazz 클래스의 ${property.name}에 할당할 수 있는 종속 항목이 존재하지 않습니다.")
            if (property is KMutableProperty1) {
                property.setter.isAccessible = true
                property.setter.call(this, argument)
            }
        }
    }

    private fun Any.injectFunction(instances: List<Any>) {
        val functions = findAnnotatedFunctions<Inject>()
        functions.forEach { function ->
            val arguments = function.getArguments(instances)
            function.isAccessible = true
            function.call(this, *arguments.toTypedArray())
        }
    }

    override fun equals(other: Any?): Boolean =
        if (other is SheathComponent) clazz == other.clazz else false

    override fun hashCode(): Int = clazz.hashCode()

    override fun toString(): String = "SheathComponent(clazz=$clazz)"
}

data class DependingCondition(
    val isSingleton: Boolean = true,
    val qualifiedName: String? = null,
) {
    companion object {
        fun from(element: KAnnotatedElement): DependingCondition =
            DependingCondition(element.hasAnnotation<Prototype>(), element.getQualifiedName())
    }
}

private fun KAnnotatedElement.getQualifiedName(): String? {
    val qualifiedName = this.findAnnotation<Qualifier>()?.value
    if (qualifiedName != null) return qualifiedName

    val annotationAttachedQualifier = this.annotations
        .find { it.annotationClass.hasAnnotation<Qualifier>() } ?: return null

    return annotationAttachedQualifier.annotationClass
        .findAnnotation<Qualifier>()
        ?.value
}
