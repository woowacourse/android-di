package com.angrypig.autodi.autoDIModule

import com.angrypig.autodi.LifeCycleType
import kotlin.reflect.KType

@Suppress("UNCHECKED_CAST")
sealed class LifeCycleTypes {
    abstract val value: MutableList<out LifeCycleType<*>>
    internal fun <T : Any> searchWithOutQualifier(kType: KType): LifeCycleType<T>? {
        return value.find { it.type == kType } as LifeCycleType<T>?
    }

    internal fun <T : Any> searchWithQualifier(kType: KType, qualifier: String): LifeCycleType<T>? {
        return value.find { it.type == kType && it.qualifier == qualifier } as LifeCycleType<T>?
    }

    class Singletons(override val value: MutableList<LifeCycleType.Singleton<*>>) :
        LifeCycleTypes() {
        internal fun <T : Any> add(qualifier: String? = null, initializeMethod: () -> T) {
            value.add(LifeCycleType.Singleton(qualifier, initializeMethod))
        }
    }

    class Disposables(override val value: MutableList<LifeCycleType.Disposable<*>>) :
        LifeCycleTypes() {
        internal fun <T : Any> add(qualifier: String? = null, initializeMethod: () -> T) {
            value.add(LifeCycleType.Disposable(qualifier, initializeMethod))
        }
    }
//    class Activities(override val values: MutableList<LifeCycleType<*>>) : LifeCycleTypes()
//    class Fragments(override val values: MutableList<LifeCycleType<*>>) : LifeCycleTypes()
}
