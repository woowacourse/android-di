package org.library.haeum.di

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.library.haeum.Container
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

abstract class DIActivity(
    private val activityModule: Module,
    val viewModelModule: Module,
) : AppCompatActivity() {
    private val container: Container by lazy {
        activityModule.setContext(this)
        Container(activityModule)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this@DIActivity::class.declaredMemberProperties
            .filter { it.hasAnnotation<HaeumInject>() }
            .forEach { property ->
                val mutableProperty = property as KMutableProperty1<*, *>
                mutableProperty.isAccessible = true
                mutableProperty.setter.call(this@DIActivity, container.resolveDependency(mutableProperty))
            }
    }

    inline fun <reified T : ViewModel> ComponentActivity.createViewModel(): T {
        val viewModel: T by viewModels {
            viewModelFactory {
                initializer {
                    Container(viewModelModule).injectTo(T::class)
                }
            }
        }
        return viewModel
    }
}
