package com.example.alsonglibrary2.di

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.alsonglibrary2.di.anotations.ActivityScope
import com.example.alsonglibrary2.di.anotations.FieldInject
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

class ActivityLifecycleObserver : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        injectDependencies(owner)
    }

    private fun injectDependencies(owner: LifecycleOwner) {
        val properties =
            owner::class.declaredMemberProperties
                .filter { it.hasAnnotation<FieldInject>() }

        properties.forEach { property ->
            property.isAccessible = true
            property.javaField?.set(
                owner,
                AutoDIManager.dependencies[property.returnType.jvmErasure],
            )
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        deleteDependencies(owner)
    }

    private fun deleteDependencies(owner: LifecycleOwner) {
        val properties =
            owner::class.declaredMemberProperties.filter { it.hasAnnotation<ActivityScope>() }

        properties.forEach { property ->
            AutoDIManager.removeDependency(property.returnType.jvmErasure)
        }
    }
}
