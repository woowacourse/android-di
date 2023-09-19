package com.hyegyeong.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hyegyeong.di.annotations.Inject
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

abstract class DIActivity(private var module: DiModule) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        module.context = this
        val container = DiContainer(module)
        Injector.container = container

        val properties: List<KProperty<*>> =
            this::class.declaredMemberProperties.filter { it.hasAnnotation<Inject>() }
                .map { it.apply { it.isAccessible = true } }

        Injector.injectField(this, properties)

//        val injector = Injector(container)
//        injector.injectField(this::class)
    }
}