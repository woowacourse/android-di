package org.library.haeum.di

import android.app.Application
import org.library.haeum.Container
import kotlin.reflect.full.declaredMemberFunctions

abstract class DIApplication(
    private val applicationModule: Module,
) : Application() {
    private val container by lazy {
        applicationModule.setContext(this)
        Container(applicationModule)
    }

    override fun onCreate() {
        super.onCreate()
        applicationModule::class.declaredMemberFunctions.forEach {
            container.invokeProvider(it)
        }
    }
}
