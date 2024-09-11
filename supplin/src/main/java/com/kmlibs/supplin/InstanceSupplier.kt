package com.kmlibs.supplin

import com.kmlibs.supplin.annotations.Supply
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import kotlin.reflect.jvm.kotlinProperty

object InstanceSupplier {
    private const val EXCEPTION_PROPERTY_NOT_FOUND = "Property that needs to be injected not found"
    private const val EXCEPTION_NO_TARGET_CONSTRUCTOR =
        "No constructor with @Supply annotation found for %s"

    /**
     * For injecting fields with @Supply annotation.
     */
    fun <T : Any> injectFields(
        clazz: Class<T>,
        targetInstance: Any,
    ) {
        clazz.declaredFields.filter { field ->
            hasSupplyAnnotation(field)
        }.forEach { targetField ->
            injectSingleField(targetField, targetInstance)
        }
    }

    private fun hasSupplyAnnotation(field: Field): Boolean =
        field.annotations.any { annotation ->
            annotation.annotationClass == Supply::class
        }

    private fun injectSingleField(
        field: Field,
        targetInstance: Any,
    ) {
        field.isAccessible = true
        field.set(targetInstance, findInstanceOf(field))
    }

    private fun findInstanceOf(field: Field): Any =
        Injector.instanceContainer.instanceOf(
            field.kotlinProperty ?: error(EXCEPTION_PROPERTY_NOT_FOUND),
        )

    // TODO 추후 생성자 주입 + 필드 주입 혼합하여 사용할 수 있도록 수정
    fun <T : Any> injectedInstance(clazz: Class<T>): T {
        val targetConstructor = targetConstructor(clazz)
        val constructorParameters = targetConstructor.parameters
        val parameterValues =
            constructorParameters.map { parameter ->
                val parameterClass = parameter.type.kotlin
                Injector.instanceContainer.instanceOf<T>(parameterClass)
            }.toTypedArray<Any>()

        val instance = targetConstructor.newInstance(*parameterValues)
        injectFields(clazz, instance)

        return instance as T
    }

    // TODO 추후 생성자 주입 + 필드 주입 혼합하여 사용할 수 있도록 수정
    private fun <T : Any> targetConstructor(clazz: Class<T>): Constructor<*> {
        val constructors = clazz.constructors
        val targetConstructor =
            constructors.firstOrNull { constructor ->
                constructor.annotations.any { annotation ->
                    annotation.annotationClass == Supply::class
                }
            }

        return checkNotNull(targetConstructor) {
            EXCEPTION_NO_TARGET_CONSTRUCTOR.format(clazz.simpleName)
        }
    }
}
