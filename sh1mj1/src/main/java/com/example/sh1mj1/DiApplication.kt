package com.example.sh1mj1

import android.app.Application
import com.example.sh1mj1.container.AppContainer
import com.example.sh1mj1.container.DefaultAppContainer
import com.example.sh1mj1.container.activityscope.DefaultActivityComponentContainer
import com.example.sh1mj1.container.viewmodelscope.ViewModelComponentContainer

open class DiApplication : Application() {
    open lateinit var container: AppContainer
    open lateinit var activityContainer: DefaultActivityComponentContainer
    open lateinit var viewModelComponentContainer: ViewModelComponentContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
        activityContainer = DefaultActivityComponentContainer.instance()
        viewModelComponentContainer = ViewModelComponentContainer.instance()
    }
}
