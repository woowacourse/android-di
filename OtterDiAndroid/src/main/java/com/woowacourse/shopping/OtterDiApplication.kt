package com.woowacourse.shopping

import android.app.Application
import woowacourse.shopping.otterdi.Injector

open class OtterDiApplication(private val module: AndroidModule? = null) : Application() {

    override fun onCreate() {
        super.onCreate()
        module?.context = this
        injector = if (module == null) Injector() else Injector(module)
    }

    companion object {
        lateinit var injector: Injector
    }
}
