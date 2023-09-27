package com.angrypig.autodi.lifeCycleScopeHandler

import androidx.lifecycle.ViewModel
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

abstract class AutoDiScopedViewModel<T : ViewModel> : ViewModel() {

    abstract val registerScope: T

    override fun onCleared() {
        super.onCleared()
        disconnectPropertyInjectionReference()
        disconnectConstructorInjectionReference()
    }

    private fun disconnectConstructorInjectionReference() {
        val primaryConstructor =
            registerScope::class.primaryConstructor
                ?: throw IllegalStateException(PRIMARY_CONSTRUCTOR_NULL_ERROR.format(registerScope.javaClass.name))
        if (primaryConstructor.findAnnotation<Scoped>() != null) {
            primaryConstructor.parameters.forEach { parameter ->
                val field = registerScope::class.java.getDeclaredField(
                    parameter.name ?: throw IllegalStateException(
                        PRIMARY_CONSTRUCTOR_PROPERTY_NAME_NULL_ERROR,
                    ),
                )
                field.isAccessible = true
                field.set(registerScope, null)
            }
        }
    }

    private fun disconnectPropertyInjectionReference() {
        for (property in registerScope::class.declaredMemberProperties) {
            val scopedAnnotation = property.findAnnotation<ScopedProperty>()
            val clazz: Class<out T> = registerScope::class.java
            if (scopedAnnotation != null) {
                val field = when (scopedAnnotation.delegated) {
                    true -> clazz.getDeclaredField(DELEGATE_PROPERTY_NAME.format(property.name))
                    false -> clazz.getDeclaredField(property.name)
                }
                field.isAccessible = true
                field.set(registerScope, null)
            }
        }
    }

    companion object {
        private const val DELEGATE_PROPERTY_NAME = "%s\$delegate"
        private const val PRIMARY_CONSTRUCTOR_NULL_ERROR =
            "%s의 주생성자가 Null로 입력되고 있습니다."
        private const val PRIMARY_CONSTRUCTOR_PROPERTY_NAME_NULL_ERROR =
            "주생성자에 선언된 필드의 name이 null값으로 입력되고 있습니다."
    }
}
