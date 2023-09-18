package com.ssu.androidi.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ssu.androidi.application.DiApplication
import com.ssu.androidi.container.DefaultContainer
import com.ssu.di.injector.Injector

abstract class DiActivity : AppCompatActivity() {
    lateinit var injector: Injector
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector = Injector((application as DiApplication).injector.container, DefaultContainer())
    }

    abstract fun addModuleInstances()
    abstract fun injectFields()

    override fun onDestroy() {
        super.onDestroy()

        injector.removeDependency()
    }
}
