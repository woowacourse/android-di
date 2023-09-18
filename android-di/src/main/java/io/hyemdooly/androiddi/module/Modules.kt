package io.hyemdooly.androiddi.module

import android.content.Context
import io.hyemdooly.di.Module

interface Modules {
    val applicationModule: (Context) -> Module
    val activityModule: (Module, Context) -> Module
    val viewModelModule: (Module) -> Module
}
