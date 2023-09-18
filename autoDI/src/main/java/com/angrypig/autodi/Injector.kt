package com.angrypig.autodi

import kotlin.reflect.KProperty

class Injector(val qualifier: String? = null) {
    internal inline operator fun <reified T : Any> getValue(thisRef: Any?, property: KProperty<*>): T {
        @Suppress("UNCHECKED_CAST")
        return AutoDI.inject(qualifier)
    }
}
