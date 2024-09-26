package com.zzang.di.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zzang.di.DIContainer
import com.zzang.di.DependencyInjector

abstract class DIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    protected open fun injectDependencies() {
        DependencyInjector.injectDependencies(this, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        DIContainer.clearActivityScopedInstances(this)
    }
}
