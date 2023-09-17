package com.di.berdi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.Field

abstract class DIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModelField =
            this::class.java.declaredFields.firstOrNull { ViewModel::class.java.isAssignableFrom(it.type) }

        viewModelField?.let {
            val viewModel = getNewViewModelByType(viewModelField)
            setViewModelInstance(it, viewModel)
        }

        super.onCreate(savedInstanceState)
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

    private fun setViewModelInstance(viewModelField: Field, viewModel: ViewModel) {
        viewModelField.isAccessible = true
        viewModelField.set(this, viewModel)
    }
}
