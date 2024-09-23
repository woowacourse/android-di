package com.kmlibs.supplin.activity

import androidx.appcompat.app.AppCompatActivity
import com.kmlibs.supplin.Injector
import kotlin.reflect.KClass

abstract class SupplinActivity(
    vararg modules: KClass<*>,
) : AppCompatActivity() {
    init {
        Injector.init {
            activityModule(this@SupplinActivity, *modules)
        }
    }
}
