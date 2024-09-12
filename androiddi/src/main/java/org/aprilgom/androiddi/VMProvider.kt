package org.aprilgom.androiddi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.KClass

/*
class VMProvider<T : ViewModel>(
    private val viewModelStoreOwner: ViewModelStoreOwner,
    private val clazz: KClass<T>,
    private val block: () -> ViewModel,
) : Provider<T> {
    override fun get(): T {
        val viewModelFactory = viewModelFactory {
            initializer { block() }
        }
        return ViewModelProvider(viewModelStoreOwner, viewModelFactory)[clazz.java]
    }
}
 */
