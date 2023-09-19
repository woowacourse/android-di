package com.angrypig.autodi.lifeCycleScopeHandler

import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

abstract class AutoDIScopedActivity<T : AppCompatActivity> : AppCompatActivity() {

    abstract val registerScope: T

    override fun onDestroy() {
        super.onDestroy()
        disconnectReference()
    }

    private fun disconnectReference() {
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
    }
}
