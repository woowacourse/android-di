package org.aprilgom.androiddi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

class ViewModelFactory<T : ViewModel>(val clazz: KClass<T>, private val block: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(clazz.java)) {
            return block() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
