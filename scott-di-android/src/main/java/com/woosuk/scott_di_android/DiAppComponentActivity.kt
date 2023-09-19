package com.woosuk.scott_di_android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

open class DiAppComponentActivity : AppCompatActivity() {

    private val module = DiApplication.module

    private val injectableTypes =
        this::class.declaredMemberProperties.filter { it.hasAnnotation<Inject>() }
            .map { it.returnType }

    private val dependencies =
        module::class.declaredMemberFunctions
            .filter { it.hasAnnotation<ActivityScope>() }
            .filter { injectableTypes.contains(it.returnType) }
            .map { Dependency(module, it, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadActivityScopeDependencies()
        injectFields()
    }

    override fun onPause() {
        super.onPause()
        dependencies.forEach { DiContainer.destroyDependency(it) }
    }

    override fun onResume() {
        super.onResume()
        dependencies.forEach { DiContainer.addDependency(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        dependencies.forEach { DiContainer.destroyDependency(it) }
    }

    private fun injectFields() {
        val properties = this::class
            .declaredMemberProperties.filter { it.hasAnnotation<Inject>() }
        Log.d("wooseok", properties.toString())
        properties.forEach { property ->
            property.isAccessible = true
            property.javaField?.set(
                this,
                DiContainer.getDependencyInstance(
                    property.returnType.jvmErasure,
                    property.getQualifierAnnotation(),
                )
            )
        }
    }

    private fun loadActivityScopeDependencies() {
        dependencies.forEach { DiContainer.addDependency(it) }
    }
}
