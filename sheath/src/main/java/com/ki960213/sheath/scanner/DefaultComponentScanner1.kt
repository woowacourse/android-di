package com.ki960213.sheath.scanner

import android.content.Context
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Module
import com.ki960213.sheath.component.SheathComponent1
import com.ki960213.sheath.component.SheathComponentFactory
import dalvik.system.DexFile
import dalvik.system.PathClassLoader
import kotlin.reflect.KClass
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation

internal class DefaultComponentScanner1(private val context: Context) : ComponentScanner {
    override fun findAll(): List<SheathComponent1> {
        val components = mutableListOf<SheathComponent1>()
        val classLoader = context.classLoader as PathClassLoader
        val dexFile = DexFile(context.packageCodePath)
        val classNames = dexFile.entries()
        while (classNames.hasMoreElements()) {
            val className = classNames.nextElement()
            components.addComponentIfMatchTarget(className, classLoader)
        }
        return components
    }

    private fun MutableList<SheathComponent1>.addComponentIfMatchTarget(
        className: String,
        classLoader: PathClassLoader,
    ) {
        if (className.isInTargetPackage()) {
            val clazz = classLoader.loadClass(className).kotlin
            if (clazz.java.isComponent()) this.add(SheathComponentFactory.create(clazz))
            if (clazz.java.isModule()) this.addAll(extractSheathComponent(clazz))
        }
    }

    private fun String.isInTargetPackage(): Boolean =
        this.startsWith(context.packageName) && !this.contains("$")

    private fun Class<*>.isComponent(): Boolean =
        this.annotations.any { it.annotationClass == Component::class || it.annotationClass.hasAnnotation<Component>() }

    private fun Class<*>.isModule(): Boolean =
        this.annotations.any { annotation -> annotation.annotationClass == Module::class }

    private fun extractSheathComponent(clazz: KClass<*>): List<SheathComponent1> =
        clazz.functions.mapNotNull { function ->
            if (function.hasAnnotation<Component>()) SheathComponentFactory.create(function) else null
        }
}
