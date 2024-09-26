package org.aprilgom.androiddi

import androidx.lifecycle.ViewModel
import javax.inject.Inject
import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmName

class DIContainer(
    val providers: MutableMap<NamedKClass, Provider<*>>,
) {
    fun provide(clazz: KClass<*>): Any {
        val namedKClass = NamedKClass(clazz)
        return provide(namedKClass)
    }

    fun provide(
        name: String,
        clazz: KClass<*>,
    ): Any {
        val namedKClass = NamedKClass(name, clazz)
        return provide(namedKClass)
    }

    fun <VM : ViewModel> provideViewModelFactory(clazz: KClass<VM>): ViewModelFactory<VM> {
        return providers[NamedKClass(clazz)]?.get() as ViewModelFactory<VM>
    }

    private fun provide(namedKClass: NamedKClass): Any {
        val instance =
            providers[namedKClass]?.get()
                ?: throw NoSuchElementException("name: ${namedKClass.name} clazz: ${namedKClass.clazz} is not provided")
        inject(instance, instance.javaClass.kotlin)
        return instance
    }

    fun inject(
        instance: Any?,
        targetClazz: KClass<*>,
    ) = findInjectProperties(targetClazz).onSuccess { properties ->
        properties.forEach { property ->
            property.isAccessible = true
            val propertyName = getPropertyName(property)
            val propertyInstance = provide(propertyName, property.returnType.classifier as KClass<*>)
            property.setter.call(instance, propertyInstance)
        }
    }

    private fun getPropertyName(property: KMutableProperty<*>): String {
        val clazzName = (property.returnType.classifier as KClass<*>).jvmName
        val qualifierName =
            property.annotations.find { annotation ->
                annotation.annotationClass.hasAnnotation<Qualifier>()
            }.let { annotation ->
                annotation?.annotationClass?.simpleName
            }
        return qualifierName ?: clazzName
    }

    private fun findInjectProperties(targetClazz: KClass<*>) =
        runCatching {
            targetClazz.declaredMemberProperties.filter {
                val isInject = it.javaField?.isAnnotationPresent(Inject::class.java) ?: false
                isInject && it.isLateinit
            }.map {
                it as KMutableProperty<*>
            }
        }
}
