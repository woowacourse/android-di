package com.woowa.di.viewmodel

import androidx.lifecycle.ViewModel
import com.woowa.di.findQualifierClassOrNull
import javax.inject.Inject
import kotlin.reflect.jvm.kotlinProperty

inline fun <reified T : ViewModel> removeInstancesOnCleared(instance: T) {
    instance.addCloseable {
        T::class.java.declaredFields.onEach { field ->
            field.isAccessible = true
        }.filter { it.isAnnotationPresent(Inject::class.java) }.forEach { field ->
            ViewModelComponentManager2.getComponentInstance(T::class).deleteDIInstance(
                field.type.kotlin,
                field.kotlinProperty?.findQualifierClassOrNull(),
            )
        }
        ViewModelComponent2.deleteInstance(T::class)
    }
}
