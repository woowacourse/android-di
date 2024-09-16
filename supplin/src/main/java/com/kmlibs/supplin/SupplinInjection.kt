package com.kmlibs.supplin

inline fun <reified T : Any> supplinInjection(): Lazy<T> =
    lazy {
        val instance = Injector.instanceContainer.instanceOf(T::class)
        instance
    }
