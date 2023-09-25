package com.woowacourse.shopping

import android.app.Application

open class OtterDiApplication(
    private val module: AndroidModule = DefaultAndroidModule(),
) : Application() {

    private val androidInjector: AndroidInjector by lazy { AndroidInjector(this, module) }

    override fun onCreate() {
        super.onCreate()

        androidInjector.injectProperty()
    }
}
