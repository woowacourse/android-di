package com.example.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.di.annotations.ActivityScope
import com.example.di.annotations.Inject
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

open class DIAppCompatActivity : AppCompatActivity() {
    private val module = DIApplication.module

    private val injectableTypes =
        this::class
            .declaredMemberProperties
            .filter { it.hasAnnotation<Inject>() }
            .map { it.returnType }

    private val dependencies =
        module::class
            .declaredMemberFunctions
            .filter { it.hasAnnotation<ActivityScope>() }
            .filter { injectableTypes.contains(it.returnType) }
            .map { Dependency(module, it, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadActivityScopeDependencies()
        injectFields()
    }

    override fun onResume() {
        super.onResume()
        dependencies.forEach { DIContainer.addDependency(it) }
    }

    override fun onPause() {
        super.onPause()
        dependencies.forEach { DIContainer.destroyDependency(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyFields()
        dependencies.forEach { DIContainer.destroyDependency(it) }
    }

    private fun loadActivityScopeDependencies() {
        dependencies.forEach { DIContainer.addDependency(it) }
    }

    private fun injectFields() {
        val properties =
            this::class
                .declaredMemberProperties
                .filter { it.hasAnnotation<Inject>() }
        properties.forEach { property ->
            property.isAccessible = true
            property.javaField?.set(
                this,
                DIContainer.getDependencyInstance(
                    property.returnType.jvmErasure,
                    property.getQualifierAnnotation(),
                ),
            )
        }
    }

    private fun destroyFields() {
        val properties =
            this::class
                .declaredMemberProperties
                .filter { it.hasAnnotation<Inject>() }
        properties.forEach { property ->
            property.isAccessible = true
            property.javaField?.set(this, null)
        }
    }
}
