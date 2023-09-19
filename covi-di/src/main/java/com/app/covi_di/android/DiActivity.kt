package com.app.covi_di.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.covi_di.annotation.InjectField
import com.app.covi_di.core.Injector
import kotlin.reflect.KClass

abstract class DiActivity : AppCompatActivity() {

    private val clazz: KClass<out DiActivity> by lazy { this::class }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clazz.java.declaredFields.forEach {
            if (it.isAnnotationPresent(InjectField::class.java)) {
                val instanceType = it.type.kotlin
                it.isAccessible = true
                it.set(this, Injector.inject(instanceType, this))

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clazz.java.declaredFields.forEach {
        }

    }
}