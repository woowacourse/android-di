package com.m6z1.moongdi

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.UUID

abstract class BaseActivity : AppCompatActivity() {
    val activityId: String = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ScopedDependencyInjector.setContext(
            ScopedDependencyInjector.InjectionContext(
                activityId = activityId,
            ),
        )

        ScopedDependencyContainer.registerToActivity(
            activityId = activityId,
            clazz = Context::class,
            instance = this as Context,
        )

        ScopedDependencyInjector.injectField(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) {
            ScopedDependencyContainer.clearActivityScope(activityId)
            ScopedDependencyInjector.clearContext()
        }
    }
}
