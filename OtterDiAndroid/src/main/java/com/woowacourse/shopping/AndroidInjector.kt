package com.woowacourse.shopping

import android.content.Context
import woowacourse.shopping.otterdi.Injector

class AndroidInjector(
    private val context: Context,
    private val module: AndroidModule,
) {

    val injector = Injector(module)

    init {
        module.context = context
    }

    inline fun <reified T : Any> inject(): T {
        return injector.inject<T>()
    }

    fun injectProperty() {
        injector.injectProperties(context)
    }
}
