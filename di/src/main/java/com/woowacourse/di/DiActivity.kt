package com.woowacourse.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.woowacourse.di.annotations.injectFields

abstract class DiActivity : AppCompatActivity() {
    private lateinit var diApplication: DiApplication
    lateinit var diInjector: DiInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diApplication = application as DiApplication
        diInjector = diApplication.getInjector(this::class.java.name) ?: createActivityInjector()
    }

    private fun createActivityInjector(): DiInjector {
        return DiInjector(loadActivityContainer())
    }

    private fun loadActivityContainer(): DiContainer {
        return DiContainer(parentContainer = diApplication.getApplicationContainer())
    }

    fun injectModule(diModule: DiModule) {
        diInjector.addModule(diModule)
        injectFields(diInjector.diContainer, this@DiActivity)
    }

    override fun onStop() {
        super.onStop()
        when {
            isChangingConfigurations -> {
                diApplication.saveInjector(this::class.java.name, diInjector)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        when {
            isFinishing -> {
                diApplication.removeInjector(this::class.java.name)
            }
        }
    }
}
