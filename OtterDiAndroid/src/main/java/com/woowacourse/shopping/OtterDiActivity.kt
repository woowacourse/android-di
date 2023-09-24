package com.woowacourse.shopping

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel

abstract class OtterDiActivity(
    private val module: AndroidModule = DefaultAndroidModule(),
) : AppCompatActivity() {

    val androidInjector: AndroidInjector by lazy { AndroidInjector(this, module) }

    inline fun <reified VM : ViewModel> viewModels(): Lazy<VM> {
        return viewModels(androidInjector)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
