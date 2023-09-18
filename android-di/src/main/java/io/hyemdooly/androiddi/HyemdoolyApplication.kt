package io.hyemdooly.androiddi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.hyemdooly.androiddi.module.Injectors

abstract class HyemdoolyApplication(injectors: Injectors) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
