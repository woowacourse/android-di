package com.yrsel.di

import com.yrsel.di.annotation.ActivityScope
import com.yrsel.di.annotation.ApplicationScope
import com.yrsel.di.annotation.SingletonScope
import com.yrsel.di.annotation.ViewModelScope
import kotlin.reflect.KClass

sealed class ScopeType {
    data object Singleton : ScopeType()

    data object UnScoped : ScopeType()

    data class Activity(
        val identifier: Any? = null,
    ) : ScopeType()

    data class ViewModel(
        val identifier: String? = null,
    ) : ScopeType()

    companion object {
        fun from(scope: KClass<out Annotation>?): ScopeType =
            when (scope) {
                ApplicationScope::class, SingletonScope::class -> Singleton
                ActivityScope::class -> Activity()
                ViewModelScope::class -> ViewModel()
                else -> UnScoped
            }
    }
}
