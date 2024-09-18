package com.kmlibs.supplin

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment

/**
 * For supplying dependencies that don't utilize other dependencies stored in `Modules`.
 *
 * If you want to supply dependencies that utilize the dependencies stored in `Modules`,
 * use field injection with late initialization and `@Supply` annotation instead.
 */
inline fun <reified T : Any> supplinInjection(): Lazy<T> =
    lazy {
        val instance = Injector.instanceContainer.instanceOf(T::class)
        instance
    }
