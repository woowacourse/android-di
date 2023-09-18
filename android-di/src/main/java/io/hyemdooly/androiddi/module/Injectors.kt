package io.hyemdooly.androiddi.module

import android.content.Context
import io.hyemdooly.di.Module

interface Injectors {
    val applicationModule: (Context) -> Module
    val retainedActivityModule: (Module, Context) -> Module
    val activityModule: (Module, Context) -> Module
    val viewModelModule: (Context) -> Module
}
