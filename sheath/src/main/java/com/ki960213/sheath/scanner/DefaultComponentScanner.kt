package com.ki960213.sheath.scanner

import android.content.Context
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.component.SheathComponent
import dalvik.system.DexFile
import dalvik.system.PathClassLoader
import kotlin.reflect.KClass

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
            if (clazz.java.isComponent()) this.add(SheathComponent(clazz))
        }
    }

    private fun String.isInTargetPackage(): Boolean =
        this.startsWith(context.packageName) && !this.contains("$")

    private fun Class<*>.isComponent(): Boolean =
        this.annotations.any { it.annotationClass == Component::class || it.hasAnnotation(Component::class) }

    private fun Annotation.hasAnnotation(annotationClass: KClass<*>): Boolean =
        this.annotationClass.annotations.any { it.annotationClass == annotationClass }
}
