package com.example.seogi.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seogi.di.DiApplication.Companion.diContainer
import com.example.seogi.di.DiApplication.Companion.module
import com.example.seogi.di.annotation.ActivityScoped
import com.example.seogi.di.annotation.FieldInject
import com.example.seogi.di.util.findDependencyFunctions
import java.lang.reflect.Field
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

open class DiActivity : AppCompatActivity() {
    private val injectProperties =
        this::class.declaredMemberProperties.filter { it.annotations.contains(FieldInject()) }
            .map { it as KMutableProperty1 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectActivityField()
        injectViewModelInstance(findViewModelField())
    }

    private fun injectActivityField() {
        injectProperties.forEach {
            val instance = diContainer.instance(it.returnType.jvmErasure)
            it.isAccessible = true
            it.setter.call(this@DiActivity, instance)
        }
    }

    private fun findViewModelField() =
        this::class.declaredMemberProperties.firstOrNull {
            val fieldType = requireNotNull(it.javaField?.type)
            ViewModel::class.java.isAssignableFrom(fieldType)
        }?.javaField

    private fun injectViewModelInstance(viewModelProperty: Field?) {
        viewModelProperty?.let { field ->
            field.isAccessible = true
            val viewModelClass = field.type as Class<ViewModel>
            val viewModelInstance =
                ViewModelProvider(this, ViewModelFactory(diContainer))[viewModelClass]
            field.set(this@DiActivity, viewModelInstance)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val injectPropertyTypes = injectProperties.map { it.returnType }
        val dependencies = module.findDependencyFunctions(ActivityScoped(), injectPropertyTypes)

        dependencies.forEach {
            diContainer.removeDependency(it)
        }
    }
}
