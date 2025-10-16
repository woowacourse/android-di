package com.example.di.util

import android.annotation.SuppressLint
import com.example.di.annotation.Inject
import com.example.di.annotation.Module
import com.example.di.annotation.Provides
import com.example.di.annotation.Qualifier
import com.example.di.annotation.Singleton
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexFile
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

/**
 * 파라미터/프로퍼티에서 Qualifier 어노테이션(커스텀) 0~1개만 허용
 */
internal fun findSingleQualifierOrNull(annotations: List<Annotation>): KClass<out Annotation>? {
    val qualifiers = annotations.filter { it.annotationClass.findAnnotation<Qualifier>() != null }
    require(qualifiers.size <= 1) { "Qualifier는 최대 1개만 허용됩니다. annotations=$annotations" }
    return qualifiers.firstOrNull()?.annotationClass
}

internal fun <T : Any> requireModuleObject(module: KClass<T>): T {
    val instance = module.objectInstance
    requireNotNull(instance) { "모듈은 object여야 합니다: ${module.qualifiedName}" }
    return instance
}

internal fun KClass<*>.isModuleObject(): Boolean = hasAnnotation<Module>() && objectInstance != null

internal fun KFunction<*>.isProvidesFunction(): Boolean = hasAnnotation<Provides>()

internal fun KFunction<*>.isSingletonFunction(): Boolean = hasAnnotation<Singleton>()

internal fun KParameter.requireInject(): Boolean = hasAnnotation<Inject>()

internal fun KProperty1<Any, Any?>.requireInject(): Boolean = hasAnnotation<Inject>()

/** BaseDexClassLoader에서 dexElements → dexFile을 꺼낸다. */
@SuppressLint("DiscouragedPrivateApi")
internal fun ClassLoader.extractDexFiles(): List<DexFile> {
    val result = mutableListOf<DexFile>()
    val base = this as? BaseDexClassLoader ?: return result

    val pathListField = BaseDexClassLoader::class.java.getDeclaredField("pathList")
    pathListField.isAccessible = true
    val pathListObj = pathListField.get(base)

    val dexElementsField = pathListObj.javaClass.getDeclaredField("dexElements")
    dexElementsField.isAccessible = true

    val dexElements = dexElementsField.get(pathListObj) as Array<*>
    dexElements.forEach { element ->
        if (element == null) return@forEach
        val dexFile =
            runCatching {
                element.javaClass
                    .getDeclaredField("dexFile")
                    .apply { isAccessible = true }
                    .get(element) as DexFile
            }.getOrNull()

        if (dexFile != null) result += dexFile
    }
    return result
}
