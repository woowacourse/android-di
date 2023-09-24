package com.woowacourse.shopping

import android.app.Application

open class OtterDiApplication(
    private val module: AndroidModule = DefaultAndroidModule(),
) : Application() {

    private lateinit var androidInjector: AndroidInjector

    override fun onCreate() {
        super.onCreate()

        androidInjector = AndroidInjector(this, module)
        androidInjector.injectProperty()
    }
}
