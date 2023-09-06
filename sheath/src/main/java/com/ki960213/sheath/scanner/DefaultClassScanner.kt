package com.ki960213.sheath.scanner

import android.content.Context
import dalvik.system.DexFile
import dalvik.system.PathClassLoader
import java.lang.reflect.Modifier

class DefaultClassScanner(private val context: Context) : ClassScanner {

    override fun findAll(target: Class<*>): List<Class<*>> {
        val classes = mutableListOf<Class<*>>()
        val classLoader = context.classLoader as PathClassLoader
        val dexFile = DexFile(context.packageCodePath)
        val classNames = dexFile.entries()
        while (classNames.hasMoreElements()) {
            val className = classNames.nextElement()
            classes.addClassIfMatchTarget(className, target, classLoader)
        }
        return classes
    }

    private fun MutableList<Class<*>>.addClassIfMatchTarget(
        className: String,
        target: Class<*>,
        classLoader: PathClassLoader,
    ) {
        if (className.isInTargetPackage()) {
            val clazz = classLoader.loadClass(className)
            if (clazz.isTarget(target)) {
                this.add(clazz)
            }
        }
    }

    private fun String.isInTargetPackage(): Boolean =
        this.startsWith(context.packageName) && !this.contains("$")

    private fun Class<*>.isTarget(target: Class<*>): Boolean =
        target.isAssignableFrom(this) && !Modifier.isAbstract(this.modifiers)
}
