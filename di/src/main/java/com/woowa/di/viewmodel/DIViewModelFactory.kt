package com.woowa.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.woowa.di.findQualifierClassOrNull
import java.lang.reflect.Field
import javax.inject.Inject
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.kotlinProperty

inline fun <reified T : ViewModel> getDIViewModelFactory(): ViewModelProvider.Factory {
    val instance = T::class.createInstance()

    val fields =
        T::class.java.declaredFields.onEach { field ->
            field.isAccessible = true
        }.filter { it.isAnnotationPresent(Inject::class.java) }

    injectFields<T>(fields, instance)
    removeInstanceOnCleared<T>(instance, fields)
    return viewModelFactory {
        addInitializer(T::class) {
            instance
        }
    }
}

inline fun <reified T : ViewModel> injectFields(
    fields: List<Field>,
    instance: T,
) {
    fields.map { field ->
        val fieldInstance =
            ViewModelComponentManager.getDIInstance(field.type.kotlin, field.kotlinProperty?.findQualifierClassOrNull())
        field.set(instance, fieldInstance)
    }
}

inline fun <reified T : ViewModel> removeInstanceOnCleared(
    instance: T,
    fields: List<Field>,
) {
    instance.addCloseable {
        fields.forEach { field ->
            ViewModelComponentManager.deleteDIInstance(
                field.type.kotlin,
                field.kotlinProperty?.findQualifierClassOrNull(),
            )
        }
    }
}
