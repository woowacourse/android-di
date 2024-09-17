package com.woowa.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.woowa.di.findQualifierClassOrNull
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.kotlinProperty

inline fun <reified T : ViewModel> getDIViewModelFactory(): ViewModelProvider.Factory {
    val binders = mutableListOf<KClass<*>>()
    val instance = T::class.createInstance()

    T::class.java.declaredFields.onEach { field ->
        field.isAccessible = true
    }.filter { it.isAnnotationPresent(Inject::class.java) }
        .map { field ->
            val binderType = ViewModelComponentManager.getComponentType(field.type.kotlin)
            binders.add(binderType)
            val fieldInstance =
                ViewModelComponent.getInstance(binderType)
                    .getDIInstance(field.type.kotlin, field.kotlinProperty?.findQualifierClassOrNull())
            field.set(instance, fieldInstance)
        }

    instance.addCloseable {
        binders.forEach {
            ViewModelComponent.deleteInstance(it)
        }
    }

    return viewModelFactory {
        addInitializer(T::class) {
            instance
        }
    }
}
