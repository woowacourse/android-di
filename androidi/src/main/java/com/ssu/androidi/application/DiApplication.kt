package com.ssu.androidi.application

import android.app.Application
import com.ssu.androidi.container.DefaultContainer
import com.ssu.di.injector.Injector

open class DiApplication : Application() {
    companion object {
        val injector: Injector = Injector(DefaultContainer())
    }
}
