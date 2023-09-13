package com.ki960213.sheath.scanner

import android.content.Context
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Module
import com.ki960213.sheath.component.SheathComponent
import com.ki960213.sheath.component.SheathComponentByClass
import com.ki960213.sheath.component.SheathComponentByFunction
import dalvik.system.DexFile
import dalvik.system.PathClassLoader
import kotlin.reflect.KClass
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation

class DefaultComponentScanner(private val context: Context) : ComponentScanner {
    override fun findAll(): List<SheathComponent> {
        val components = mutableListOf<SheathComponent>()
        val classLoader = context.classLoader as PathClassLoader
        val dexFile = DexFile(context.packageCodePath)
        val classNames = dexFile.entries()
        while (classNames.hasMoreElements()) {
            val className = classNames.nextElement()
            components.addComponentIfMatchTarget(className, classLoader)
        }
        return components
    }

    private fun MutableList<SheathComponent>.addComponentIfMatchTarget(
        className: String,
        classLoader: PathClassLoader,
    ) {
        if (className.isInTargetPackage()) {
            val clazz = classLoader.loadClass(className).kotlin
            if (clazz.java.isComponent()) this.add(SheathComponentByClass(clazz))
            if (clazz.java.isModule()) this.addAll(extractSheathComponent(clazz))
        }
    }

    private fun String.isInTargetPackage(): Boolean =
        this.startsWith(context.packageName) && !this.contains("$")

    private fun Class<*>.isComponent(): Boolean =
        this.annotations.any { it.annotationClass == Component::class || it.annotationClass.hasAnnotation<Component>() }

    private fun Class<*>.isModule(): Boolean =
        this.annotations.any { annotation -> annotation.annotationClass == Module::class }

    private fun extractSheathComponent(clazz: KClass<*>): List<SheathComponent> =
        clazz.functions.mapNotNull { function ->
            if (function.hasAnnotation<Component>()) SheathComponentByFunction(function) else null
        }
}
