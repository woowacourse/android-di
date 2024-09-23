package org.library.haeum.di

import android.content.Context

abstract class Module {
    var context: Context? = null
        private set

    fun setContext(context: Context) {
        this.context = context
    }
}
