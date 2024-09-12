package org.library.haeum

import android.content.Context
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

object ModuleInjector {
    lateinit var container: Container

    fun initializeModuleInjector(
        context: Context,
        vararg modules: KClass<out Any>,
    ) {
        val instances =
            modules.filter { it.hasAnnotation<Module>() }.mapNotNull {
                it.objectInstance ?: if (it.isCompanion) {
                    it.java.enclosingClass.kotlin.objectInstance
                } else {
                    null
                }
            }
        container = Container(context, instances)
    }
}