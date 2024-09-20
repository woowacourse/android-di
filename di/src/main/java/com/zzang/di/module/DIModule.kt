package com.zzang.di.module

import com.zzang.di.DIContainer

interface DIModule {
    fun register(container: DIContainer)
}
