package com.ssu.androidi.application

import android.app.Application
import com.ssu.androidi.container.DefaultContainer
import com.ssu.di.injector.Injector

open class DiApplication : Application() {
    val injector: Injector = Injector(DefaultContainer())
}
