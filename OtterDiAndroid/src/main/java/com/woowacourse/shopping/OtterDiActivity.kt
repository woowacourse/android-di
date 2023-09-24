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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        androidInjector = AndroidInjector(this, module)
        initProperties()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this.isChangingConfigurations) {
            InstancesContainer.saveInstances(this)
        } else {
            InstancesContainer.clear()
        }
    }

    private fun initProperties() {
        if (InstancesContainer.isEmpty()) {
            androidInjector.injectProperty()
        } else {
            InstancesContainer.setInstances(this)
        }
    }
}
