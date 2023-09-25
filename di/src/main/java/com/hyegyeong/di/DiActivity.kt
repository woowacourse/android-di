package com.hyegyeong.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class DiActivity : AppCompatActivity() {
    abstract val module: DiModule
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DiContainer.dependencyModule = module
    }
}
