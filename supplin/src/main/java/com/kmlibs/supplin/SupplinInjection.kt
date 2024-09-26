package com.kmlibs.supplin

import androidx.activity.ComponentActivity

/**
 * For supplying dependencies that don't utilize other dependencies stored in `Modules`.
 *
 * If you want to supply dependencies that utilize the dependencies stored in `Modules`,
 * use field injection with late initialization and `@Supply` annotation instead.
 */
inline fun <reified T : Any> ComponentActivity.supplinInjection() =
    lazy {
        requireNotNull(Injector.componentContainers[this::class]?.instanceOf(T::class)) {
            "No instance of ${T::class.simpleName} found"
        }
    }
