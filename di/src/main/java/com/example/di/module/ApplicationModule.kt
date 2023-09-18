package com.example.di.module

import android.content.Context
import com.example.di.application.DiApplication

abstract class ApplicationModule(val applicationContext: Context) : Module() {
    fun inject(application: DiApplication) { // 액티비티에 필드 주입해주는 메소드
        provideInjectField(application)
    }
}
