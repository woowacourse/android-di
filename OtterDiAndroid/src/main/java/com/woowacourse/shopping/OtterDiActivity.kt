package com.woowacourse.shopping

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.otterdi.Injector

abstract class OtterDiActivity(private val module: AndroidModule? = null) : AppCompatActivity() {

    lateinit var injector: Injector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        module?.context = this
        injector = if (module != null) Injector(module) else Injector()
        injector.injectProperties(this)
    }

    @MainThread
    inline fun <reified VM : ViewModel> viewModels(): Lazy<VM> {
        val viewModelFactory = viewModelFactory {
            initializer {
                injector.inject<VM>()
            }
        }
        return ViewModelLazy(
            VM::class,
            { viewModelStore },
            { viewModelFactory },
        )
    }
}
