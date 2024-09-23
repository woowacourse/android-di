package com.kmlibs.supplin.model

import android.app.Service
import androidx.activity.ComponentActivity
import kotlin.reflect.KClass

sealed interface Scope {
    data object Application : Scope
    data object Activity : Scope
    data object ViewModel : Scope
}
