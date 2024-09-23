package com.example.sh1mj1.component.viewmodelscope

import com.example.sh1mj1.annotation.Qualifier
import kotlin.reflect.KClass

data class ViewModelScopeComponent<T : Any>(
    val injectedClass: KClass<T>,
    val instance: T,
    val qualifier: Qualifier? = null,
)