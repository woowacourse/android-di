package io.hyemdooly.androiddi.module

import android.content.Context
import io.hyemdooly.androiddi.element.FakeFormatterInActivity
import io.hyemdooly.di.Module
import io.hyemdooly.di.annotation.Singleton

class FakeActivityModule(parentModule: Module, private val activityContext: Context) : Module(parentModule) {
    @Singleton
    fun provideContext() = activityContext

    @Singleton
    fun provideFormatter() = FakeFormatterInActivity()
}
