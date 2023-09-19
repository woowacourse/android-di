package com.bignerdranch.android.koala

import android.app.Application
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation

open class DiApplication(
    private val module: DiModule,
) : Application() {

    override fun onCreate() {
        super.onCreate()

        module.context = this
        injector = Injector(module)
        setupSingleton()
    }

    private fun setupSingleton() {
        val singletons =
            module::class.declaredMemberFunctions.filter { it.hasAnnotation<KoalaSingleton>() }
        singletons.forEach { func ->
            injector.callFunction(func)
        }
    }

    companion object {
        lateinit var injector: Injector
    }
}
