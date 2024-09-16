package com.kmlibs.supplin

import com.kmlibs.supplin.annotations.Supply
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

object InstanceSupplier {
    /**
     * For injecting fields with @Supply annotation.
     */
    fun <T : Any> injectFields(
        clazz: KClass<T>,
        targetInstance: Any,
    ) {
        clazz.memberProperties.filter { field ->
            field.hasAnnotation<Supply>()
        }.forEach { targetField ->
            injectSingleField(targetField as KMutableProperty<*>, targetInstance)
        }
    }

    private fun injectSingleField(
        property: KMutableProperty<*>,
        targetInstance: Any,
    ) {
        property.setter.call(targetInstance, findInstanceOf(property))
    }

    private fun findInstanceOf(property: KProperty<*>): Any {
        return Injector.instanceContainer.instanceOf(property)
    }

    //    TODO 추후 생성자 주입 + 필드 주입 혼합하여 사용할 수 있도록 수정
//    fun <T : Any> injectedInstance(clazz: KClass<T>): T {
//        val targetConstructor = targetConstructor(clazz)
//        val constructorParameters = targetConstructor.parameters
//        val parameterValues =
//            constructorParameters.map { parameter ->
//                val parameterClass = parameter.type
//                Injector.instanceContainer.instanceOf(parameterClass)
//            }.toTypedArray<Any>()
//
//        val instance = targetConstructor.newInstance(*parameterValues)
//        injectFields(clazz, instance)
//
//        return instance as T
//    }
//
//    private fun <T : Any> targetConstructor(clazz: KClass<T>): KFunction<*> {
//        val constructors = clazz.constructors
//        val targetConstructor =
//            constructors.firstOrNull { constructor ->
//                constructor.annotations.any { annotation ->
//                    annotation.annotationClass == Supply::class
//                }
//            }
//
//        return checkNotNull(targetConstructor) {
//            EXCEPTION_NO_TARGET_CONSTRUCTOR.format(clazz.simpleName)
//        }
//    }
//    private const val EXCEPTION_PROPERTY_NOT_FOUND = "Property that needs to be injected not found"
//    private const val EXCEPTION_NO_TARGET_CONSTRUCTOR =
//        "No constructor with @Supply annotation found for %s"
}
