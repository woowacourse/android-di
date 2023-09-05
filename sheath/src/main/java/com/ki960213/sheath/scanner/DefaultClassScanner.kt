package com.ki960213.sheath.scanner

import android.content.Context
import dalvik.system.DexFile
import dalvik.system.PathClassLoader
import java.lang.reflect.Modifier

class DefaultClassScanner(private val context: Context) : ClassScanner {

    override fun findAll(targetClass: Class<*>): List<Class<*>> {
        val classes = mutableListOf<Class<*>>()
        val classLoader = context.classLoader as PathClassLoader
        val dexFile = DexFile(context.packageCodePath)
        val classNames = dexFile.entries()
        while (classNames.hasMoreElements()) {
            val className = classNames.nextElement()
            if (isTargetClassName(className)) {
                val clazz = classLoader.loadClass(className)
                if (isTargetClass(targetClass, clazz)) {
                    classes.add(clazz)
                }
            }
        }
        return classes
    }

    private fun isTargetClassName(className: String): Boolean =
        className.startsWith(context.packageName) && !className.contains("$")

    private fun isTargetClass(targetClass: Class<*>, clazz: Class<*>): Boolean =
        targetClass.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.modifiers)
}
