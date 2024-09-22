package com.example.sh1mj1

import android.app.Application
import com.example.sh1mj1.container.DefaultActivityComponentContainer

open class DiApplication : Application() {
    open lateinit var activityContainer: DefaultActivityComponentContainer

    override fun onCreate() {
        super.onCreate()
        activityContainer = DefaultActivityComponentContainer.instance()
    }
}
