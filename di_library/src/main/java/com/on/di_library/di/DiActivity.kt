package com.on.di_library.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

abstract class DiActivity : AppCompatActivity() {
    val activityID: Long = System.currentTimeMillis()

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = DiViewModelFactory(activityID)

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