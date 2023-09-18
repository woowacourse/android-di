package com.example.androidi.application

import android.app.Application
import com.example.androidi.container.DefaultContainer
import woowacourse.shopping.di.injector.Injector

open class DiApplication : Application() {
    companion object {
        val injector: Injector = Injector(DefaultContainer())
    }
}
