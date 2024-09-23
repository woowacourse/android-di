package com.kmlibs.supplin.model

sealed interface Scope {
    data object Application : Scope

    data object Activity : Scope

    data object ViewModel : Scope
}
