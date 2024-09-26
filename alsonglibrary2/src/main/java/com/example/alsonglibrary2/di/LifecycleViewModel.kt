package com.example.alsonglibrary2.di

import androidx.lifecycle.ViewModel
import com.example.alsonglibrary2.di.anotations.ViewModelScope
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

open class LifecycleViewModel : ViewModel() {
    override fun onCleared() {
        super.onCleared()
        val properties =
            this::class.declaredMemberProperties.filter { it.hasAnnotation<ViewModelScope>() }
        properties.forEach { property ->
            AutoDIManager.removeDependency(property.returnType.jvmErasure)
        }
    }
}
