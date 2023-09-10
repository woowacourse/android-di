package com.ki960213.sheath.component

import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Inject
import com.ki960213.sheath.annotation.Prototype
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible

class SheathComponent(private val clazz: KClass<*>) {

    val isSingleton: Boolean = !clazz.hasAnnotation<Prototype>()

    val dependentCount: Int = getDependingClasses().size

    init {
        validateAnnotation()
    }

    fun isDependingOn(component: SheathComponent): Boolean {
        val dependingClasses = getDependingClasses()
        return dependingClasses.any { component.clazz.isSuperclassOf(it) }
    }

    fun instantiated(instances: List<Any>): Any {
        val instance = createInstanceByConstructor(instances)

        instance.injectProperty(instances)
        instance.injectFunction(instances)

        return instance
    }

    private fun validateAnnotation() {
        require(clazz.hasAnnotation<Component>() || clazz.annotations.any { annotation -> annotation.annotationClass.hasAnnotation<Component>() }) {
            "@Component가 붙은 클래스 혹은 @Component가 붙은 애노테이션이 붙은 클래스로만 SheathComponent를 생성할 수 있습니다."
        }
        require(clazz.constructors.count { it.hasAnnotation<Inject>() } <= 1) {
            "여러 개의 생성자에 @Inject 애노테이션을 붙일 수 없습니다."
        }
    }

    private inline fun <reified A : Annotation> findAnnotatedConstructor(): KFunction<Any>? =
        clazz.constructors.find { it.hasAnnotation<A>() }

    private inline fun <reified A : Annotation> findAnnotatedProperties(): List<KProperty1<*, *>> =
        clazz.declaredMemberProperties.filter { it.hasAnnotation<A>() }

    private inline fun <reified A : Annotation> findAnnotatedFunctions(): List<KFunction<*>> =
        clazz.declaredMemberFunctions.filter { it.hasAnnotation<A>() }

    private fun getDependingClasses(): List<KClass<*>> =
        getAnnotatedElements<Inject>().flatMap { element ->
            when (element) {
                is KFunction<*> -> {
                    element.valueParameters.map { it.type.classifier as KClass<*> }
                }

                is KProperty1<*, *> -> {
                    listOf(element.returnType.classifier as KClass<*>)
                }

                else -> listOf()
            }
        }.takeUnless { it.isEmpty() }
            ?: clazz.primaryConstructor?.valueParameters?.map { it.type.classifier as KClass<*> }
            ?: listOf()

    private inline fun <reified A : Annotation> getAnnotatedElements(): List<KAnnotatedElement> =
        clazz.constructors.filter { it.hasAnnotation<A>() } +
            clazz.declaredMemberProperties.filter { it.hasAnnotation<A>() } +
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
