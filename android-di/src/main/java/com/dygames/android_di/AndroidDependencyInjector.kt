package com.dygames.android_di

import android.content.Context
import com.dygames.di.DependencyInjector
import kotlin.reflect.KType
import kotlin.reflect.typeOf

object AndroidDependencyInjector {
    fun provideContext(context: Context, lifecycle: KType?) {
        DependencyInjector.lifecycleAwareDependencies.value[lifecycle]?.value?.get(null)?.value?.set(
            typeOf<Context>(), context
        )
    }
}
