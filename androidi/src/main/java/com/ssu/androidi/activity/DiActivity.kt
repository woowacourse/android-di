package com.ssu.androidi.activity

import androidx.appcompat.app.AppCompatActivity
import com.ssu.androidi.container.DefaultContainer
import com.ssu.di.injector.Injector

open class DiActivity : AppCompatActivity() {
    val injector: Injector = Injector(DefaultContainer())
}