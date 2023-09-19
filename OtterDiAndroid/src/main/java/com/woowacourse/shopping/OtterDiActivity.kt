package com.woowacourse.shopping

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import woowacourse.shopping.otterdi.Injector

abstract class OtterDiActivity(val module: AndroidModule? = null) : AppCompatActivity() {

    private lateinit var injector: Injector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector = if (module != null) Injector(module) else OtterDiApplication.injector
        injector.injectProperties(this::class)
    }
}
