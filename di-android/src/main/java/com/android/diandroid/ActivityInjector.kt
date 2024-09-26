package com.android.diandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.di.component.DiComponent
import com.android.di.component.DiContainer
import com.android.di.component.DiInjector
import com.android.di.component.Module

abstract class ActivityInjector : AppCompatActivity() {
    private lateinit var applicationInjector: ApplicationInjector
    lateinit var diInjector: DiInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applicationInjector = application as ApplicationInjector
        diInjector = applicationInjector.getInjector(extractKey())
            ?: createActivityInjector()
    }

    fun injectModule(module: Module) {
        if (!DiComponent.hasActivityAnnotation(javaClass.kotlin)) {
            throw IllegalArgumentException(ERROR_ANNOTATION)
        }
        diInjector.addModule(module)
        DiComponent.injectFields(diInjector.diContainer, this@ActivityInjector)
    }

    override fun onStop() {
        super.onStop()
        when {
            isChangingConfigurations -> {
                applicationInjector.saveInjector(extractKey(), diInjector)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        when {
            isFinishing -> {
                applicationInjector.removeInjector(extractKey())
            }
        }
    }

    private fun extractKey(): String = this::class.java.name

    private fun createActivityInjector(): DiInjector {
        return DiInjector(loadActivityContainer())
    }

    private fun loadActivityContainer(): DiContainer {
        return DiContainer(parentContainer = applicationInjector.getApplicationContainer())
    }

    companion object {
        private const val ERROR_ANNOTATION =
            " AppCompatActivity class must be annotated with @DiActivity"
    }
}
