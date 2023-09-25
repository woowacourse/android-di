package com.di.berdi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.di.berdi.util.declaredViewModelFields
import com.di.berdi.util.setInstance
import java.lang.reflect.Field

abstract class DIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        injectProperties()
        injectViewModel()
        super.onCreate(savedInstanceState)
    }

    private fun injectViewModel() {
        this::class.declaredViewModelFields.forEach { field ->
            field.setInstance(this, getNewViewModelByType(field))
        }
    }

    private fun injectProperties() {
        val injector = (application as DIApplication).injector
        injector.injectProperties(context = this, target = this)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getNewViewModelByType(field: Field): ViewModel {
        val vm = field.type as? Class<ViewModel>
        requireNotNull(vm) { "No matching ViewModel type | type : ${field.type}" }
        return getViewModel(vm)
    }

    private inline fun <reified VM : ViewModel> getViewModel(type: Class<VM>): VM {
        val application = application as DIApplication
        return ViewModelProvider(
            owner = this@DIActivity,
            factory = ViewModelFactory(this, application.injector),
        )[type]
    }
}
