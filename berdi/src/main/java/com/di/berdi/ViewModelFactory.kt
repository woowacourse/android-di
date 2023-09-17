package com.di.berdi

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.full.primaryConstructor

class ViewModelFactory(private val context: Context, private val injector: Injector) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = requireNotNull(modelClass.kotlin.primaryConstructor)
        return injector.createBy(context, constructor)
    }
}
