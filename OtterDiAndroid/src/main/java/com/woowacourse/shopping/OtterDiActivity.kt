package com.woowacourse.shopping

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

abstract class OtterDiActivity(
    private val module: AndroidModule = DefaultAndroidModule(),
) : AppCompatActivity() {

    lateinit var androidInjector: AndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        androidInjector = AndroidInjector(this, module)
        androidInjector.injectProperty()
    }

    @MainThread
    inline fun <reified VM : ViewModel> viewModels(): Lazy<VM> {
        val viewModelFactory = viewModelFactory {
            initializer {
                androidInjector.inject<VM>()
            }
        }
        return ViewModelLazy(
            VM::class,
            { viewModelStore },
            { viewModelFactory },
        )
    }
}
