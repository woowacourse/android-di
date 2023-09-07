package com.ki960213.sheath.scanner

import android.content.Context
import dalvik.system.DexFile
import dalvik.system.PathClassLoader
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

class DefaultClassScanner(private val context: Context) : ClassScanner {

    override fun findAll(targetClass: KClass<*>): List<KClass<*>> {
        val classes = mutableListOf<KClass<*>>()
        val classLoader = context.classLoader as PathClassLoader
        val dexFile = DexFile(context.packageCodePath)
        val classNames = dexFile.entries()
        while (classNames.hasMoreElements()) {
            val className = classNames.nextElement()
            classes.addClassIfMatchTarget(className, targetClass, classLoader)
        }
        return classes
    }

    private fun MutableList<KClass<*>>.addClassIfMatchTarget(
        className: String,
        targetClass: KClass<*>,
        classLoader: PathClassLoader,
    ) {
        if (className.isInTargetPackage()) {
            val clazz = classLoader.loadClass(className).kotlin
            if (clazz.java.isTarget(targetClass.java)) this.add(clazz)
        }
    }

    private fun String.isInTargetPackage(): Boolean =
        this.startsWith(context.packageName) && !this.contains("$")

    private fun Class<*>.isTarget(target: Class<*>): Boolean =
        target.isAssignableFrom(this) && !Modifier.isAbstract(this.modifiers)
}
