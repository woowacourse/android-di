package com.example.seogi.di


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.seogi.di.util.getAnnotationIncludeQualifier
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

class ViewModelFactory(
    private val diContainer: DiContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: throw IllegalArgumentException(ERROR_INVALID_PRIMARY_CONSTRUCTOR)

        val params =
            constructor.parameters.map { param ->
                val annotation = param.getAnnotationIncludeQualifier()
                diContainer.getInstance(param.type.jvmErasure, annotation)
            }
        val instance = constructor.call(*params.toTypedArray())
        injectFields(modelClass, instance)

        return instance
    }

    private fun <T : ViewModel> injectFields(
        modelClass: Class<T>,
        instance: T,
    ) {
        val fields =
            modelClass.kotlin.declaredMemberProperties.filter {
                it.annotations.contains(FieldInject())
            }

        fields.forEach { field ->
            val qualifierAnnotation = field.getAnnotationIncludeQualifier()
            val value = diContainer.getInstance(field.returnType.jvmErasure, qualifierAnnotation)
            field.javaField?.set(instance, value)
        }
    }

    companion object {
        private const val ERROR_INVALID_PRIMARY_CONSTRUCTOR = "No primary constructor"
    }
}
