package com.example.seogi.di

import androidx.lifecycle.ViewModel
import com.example.seogi.di.DiApplication.Companion.diContainer
import com.example.seogi.di.DiApplication.Companion.module
import com.example.seogi.di.annotation.ViewModelScoped
import com.example.seogi.di.util.fieldsToInject
import com.example.seogi.di.util.findDependencyFunctions
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor

open class DiViewModel : ViewModel() {
    override fun onCleared() {
        super.onCleared()
        val viewModelDependencyTypes = viewModelDependencyTypes()
        val dependencies =
            module.findDependencyFunctions(ViewModelScoped(), viewModelDependencyTypes)

        dependencies.forEach {
            diContainer.removeDependency(it)
        }
    }

    private fun viewModelDependencyTypes(): List<KType> {
        val constructor = this::class.primaryConstructor ?: throw IllegalArgumentException()
        val parametersType = constructor.parameters.map { it.type }
        val injectPropertiesType = this::class.fieldsToInject().map { it.returnType }

        return parametersType + injectPropertiesType
    }
}
