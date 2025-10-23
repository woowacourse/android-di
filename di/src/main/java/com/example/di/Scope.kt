package com.example.di

import kotlin.reflect.KAnnotatedElement

enum class Scope {
    APPLICATION,
    VIEWMODEL,
    ACTIVITY,
    ;

    companion object {
        fun from(element: KAnnotatedElement): Scope =
            when (Lifespan.from(element)) {
                is ViewModelLifespan -> VIEWMODEL
                is ActivityLifespan -> ACTIVITY
                else -> APPLICATION
            }
    }
}
