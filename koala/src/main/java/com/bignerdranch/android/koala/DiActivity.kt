package com.bignerdranch.android.koala

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

open class DiActivity(
    private val activityModule: DiModule,
    val viewModelModule: DiModule,
) : AppCompatActivity() {

    lateinit var injector: Injector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityModule.context = this
        injector = Injector(activityModule)
        setupParameters()
    }

    private fun setupParameters() {
        val properties =
            this::class.declaredMemberProperties.filterIsInstance<KMutableProperty1<*, *>>()
                .filter { it.hasAnnotation<KoalaFieldInject>() }
        properties.forEach { property ->
            property.isAccessible = true
            property.setter.call(this, injector.getPropertyInstance(property))
        }
    }

    inline fun <reified VM : ViewModel> viewmodel(): Lazy<VM> {
        return ViewModelLazy(
            VM::class,
            { viewModelStore },
            {
                viewModelFactory {
                    initializer {
                        Injector(viewModelModule).inject(VM::class.java.kotlin)
                    }
                }
            },
        )
    }
}
