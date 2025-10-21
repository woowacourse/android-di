package com.m6z1.moongdi

import android.app.Application
import android.content.Context
import kotlin.reflect.KClass

fun startMoong(vararg items: Any) {
    items.forEach { item ->
        when (item) {
            is Application -> {
                ScopedDependencyContainer.register(item)
                ScopedDependencyContainer.registerToApplication(Context::class, item)
            }

            is KClass<*> -> {
                try {
                    ScopedDependencyInjector.setContext(
                        ScopedDependencyInjector.InjectionContext(),
                    )

                    ScopedDependencyInjector.inject(item)

                    ScopedDependencyInjector.clearContext()
                } catch (e: Exception) {
                    ScopedDependencyInjector.clearContext()
                    throw IllegalStateException(
                        "인스턴스화 실패 ${item.simpleName}: ${e.message}",
                        e,
                    )
                }
            }

            else -> {
                ScopedDependencyContainer.register(item)
            }
        }
    }
}
