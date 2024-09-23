package com.example.seogi.di

import androidx.lifecycle.ViewModel
import com.example.seogi.di.DiApplication.Companion.diContainer
import com.example.seogi.di.DiApplication.Companion.module
import com.example.seogi.di.annotation.FieldInject
import com.example.seogi.di.annotation.ViewModelScoped
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

open class DiViewModel : ViewModel() {
    override fun onCleared() {
        super.onCleared()
        val constructor = this::class.primaryConstructor ?: throw IllegalArgumentException()
        val parametersType = constructor.parameters.map { it.type }
        val injectPropertiesType =
            this::class.declaredMemberProperties
                .filter { it.annotations.contains(FieldInject()) }
                .map { it.returnType }

        val dependenciesType = parametersType + injectPropertiesType

        val dependencies =
            module::class.declaredFunctions
                .filter { it.visibility == KVisibility.PUBLIC }
                .filter { it.annotations.contains(ViewModelScoped()) }
                .filter { dependenciesType.contains(it.returnType) }

        dependencies.forEach {
            diContainer.removeDependency(it)
        }
    }
}
