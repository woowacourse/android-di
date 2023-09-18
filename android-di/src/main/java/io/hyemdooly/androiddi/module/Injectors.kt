package io.hyemdooly.androiddi.module

import android.content.Context
import io.hyemdooly.di.Injector

interface Injectors {
    val applicationInjector: (Context) -> Injector
    val retainedActivityInjector: (Injector, Context) -> Injector
    val activityInjector: (Injector, Context) -> Injector
    val viewModelInjector: (Context) -> Injector
}
