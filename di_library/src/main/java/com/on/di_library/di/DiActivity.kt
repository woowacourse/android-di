package com.on.di_library.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class DiActivity : AppCompatActivity() {
    val activityID: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DiContainer.injectFieldProperties(
            implementClass = this::class,
            instance = this,
            scopeId = activityID
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        ScopeContainer.clearActivityScope(activityID)
    }
}