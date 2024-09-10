package woowacourse.shopping.di

import android.content.Context
import dalvik.system.DexFile
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

class AppClassLoader(context: Context): ClassLoader {
    private val allClasses: List<KClass<*>> = getAllClasses(context)

    private fun getAllClasses(context: Context): List<KClass<*>> {
        val classNames = DexFile(context.packageCodePath).entries()
        val classLoader = context.classLoader

        val result = mutableListOf<Class<*>>()

        classNames.iterator().forEach { className ->
            if (className.startsWith("woowacourse.shopping") && className.contains("$").not()) {
                val loadedClass = classLoader.loadClass(className)
                result.add(loadedClass as Class<*>)
            }
        }

        return result.map { it.kotlin }
    }

    override fun getSubclasses(parent: KClass<*>): List<KClass<*>> {
        return allClasses.filter { it.superclasses.contains(parent) }
    }
}
