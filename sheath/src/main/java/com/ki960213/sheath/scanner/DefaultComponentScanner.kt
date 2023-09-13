package com.ki960213.sheath.scanner

import android.content.Context
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Module
import com.ki960213.sheath.component.SheathComponent
import com.ki960213.sheath.component.SheathComponentByClass
import dalvik.system.DexFile
import dalvik.system.PathClassLoader
import kotlin.reflect.KClass
import kotlin.reflect.full.functions

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
        this.annotations.any { it.annotationClass == Component::class || it.hasAnnotation(Component::class) }

    private fun Class<*>.isModule(): Boolean =
        this.annotations.any { annotation -> annotation.annotationClass == Module::class }

    private fun extractSheathComponent(clazz: KClass<*>): List<SheathComponent> {
        clazz.functions.map { function ->
            val returnTypeClass = function.returnType.classifier as KClass<*>
            returnTypeClass.java
        }
        return listOf()
    }

    private fun Annotation.hasAnnotation(annotationClass: KClass<*>): Boolean =
        this.annotationClass.annotations.any { it.annotationClass == annotationClass }
}
