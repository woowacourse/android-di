package com.example.sh1mj1

import android.app.Application
import com.example.sh1mj1.container.AppContainer
import com.example.sh1mj1.container.DefaultActivityComponentContainer
import com.example.sh1mj1.container.DefaultAppContainer

open class DiApplication : Application() {
    open lateinit var container: AppContainer
    open lateinit var activityContainer: DefaultActivityComponentContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
        activityContainer = DefaultActivityComponentContainer.instance()
    }
}
