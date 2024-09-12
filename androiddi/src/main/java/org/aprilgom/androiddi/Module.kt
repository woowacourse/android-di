package org.aprilgom.androiddi

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import javax.inject.Inject
import javax.inject.Qualifier
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmName

class Module(
    val providers: Map<NamedKClass, Provider<*>>,
) {

    fun exists(clazz: KClass<*>): Boolean {
        val namedKClass = NamedKClass(clazz)
        return exists(namedKClass)
    }

    fun exists(name: String, clazz: KClass<*>): Boolean {
        val namedKClass = NamedKClass(name, clazz)
        return exists(namedKClass)
    }

    fun exists(namedKClass: NamedKClass) =
        providers.containsKey(namedKClass)

    fun inject() {
        providers.forEach { K, V ->
            inject(V.get(), K.clazz)
        }
    }

    operator fun plus(module: Module): Module {
        val newProviders = providers + module.providers
        return Module(newProviders)
    }
}
