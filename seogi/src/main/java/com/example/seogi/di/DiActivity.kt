package com.example.seogi.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seogi.di.annotation.FieldInject
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

open class DiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val injectProperties =
            this::class.declaredMemberProperties.filter { it.annotations.contains(FieldInject()) }
                .map { it as KMutableProperty1 }

        injectProperties.forEach {
            val diContainer = (application as DiApplication).diContainer
            val instance = diContainer.instance(it.returnType.jvmErasure)
            it.isAccessible = true
            it.setter.call(this@DiActivity, instance)
        }

        val viewModelProperty =
            this::class.declaredMemberProperties.firstOrNull {
                val propertyType = requireNotNull(it.javaField?.type)
                ViewModel::class.java.isAssignableFrom(propertyType)
            }?.javaField

        viewModelProperty?.let { field ->
            field.isAccessible = true
            val viewModelClass = field.type as Class<ViewModel>
            val viewModelInstance =
                ViewModelProvider(
                    this,
                    ViewModelFactory((application as DiApplication).diContainer),
                )[viewModelClass]

            field.set(this@DiActivity, viewModelInstance)
        }
    }
}
