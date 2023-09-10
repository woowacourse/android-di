package com.angrypig.autodi.autoDIModule

import androidx.lifecycle.ViewModel
import com.angrypig.autodi.ViewModelBundle
import kotlin.reflect.KType

class ViewModelBundles(value: MutableList<ViewModelBundle<*>>) {
    private val _value: MutableList<ViewModelBundle<*>> = value
    val value: List<ViewModelBundle<*>> get() = _value.toList()

    internal fun <VM : ViewModel> add(initializeMethod: () -> VM) {
        _value.add(ViewModelBundle(initializeMethod))
    }

    internal fun search(kType: KType): ViewModelBundle<*>? {
        return value.find { it.type == kType }
    }
}
