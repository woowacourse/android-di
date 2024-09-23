package com.example.yennydi.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yennydi.application.DiApplication

abstract class DiActivity : AppCompatActivity() {
    val instanceModule = ActivityInstanceModule()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(instanceModule)
//        injectDependencies() TODO: 액티비티 필드 값 주입
    }

    private fun injectDependencies() {
        val target = this::class.java
        (application as DiApplication).injector.injectProperty(target, instanceModule)
    }
}
