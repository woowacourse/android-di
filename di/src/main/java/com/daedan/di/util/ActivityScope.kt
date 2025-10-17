package com.daedan.di.util

import android.annotation.SuppressLint
import androidx.core.app.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.daedan.di.AppContainerStore
import com.daedan.di.DiApplication
import com.daedan.di.qualifier.Qualifier
import com.daedan.di.qualifier.TypeQualifier
import com.daedan.di.scope.Scope
import com.daedan.di.scope.TypeScope
import com.daedan.di.scope.UniqueScope

fun ComponentActivity.activityScope(scope: Scope = TypeScope(this::class)): Lazy<UniqueScope> =
    lazy {
        val store = (this.application as DiApplication).appContainerStore
        val uniqueScope = UniqueScope(scope, hashCode().toString())

        if (!store.isScopeOpen(uniqueScope)) {
            initialize(store, uniqueScope)
        }
        uniqueScope
    }

inline fun <reified T> ComponentActivity.inject(
    scope: Lazy<UniqueScope>,
    qualifier: Qualifier = TypeQualifier(T::class),
): Lazy<T> =
    lazy {
        val store = (this.application as DiApplication).appContainerStore
        store.instantiate(qualifier, scope.value) as T
    }

@SuppressLint("RestrictedApi")
private fun ComponentActivity.initialize(
    store: AppContainerStore,
    scope: UniqueScope,
) {
    store.createScope(scope)
    registerCurrentContext(store, scope)
    lifecycle.addObserver(
        object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                store.closeScope(scope)
                owner.lifecycle.removeObserver(this)
                super.onDestroy(owner)
            }
        },
    )
}
