package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.Inject
import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

object DependencyInjector {
    fun <T : Any> inject(targetClass: KClass<T>): T {
        val instance = DIContainer.resolve(targetClass)
        injectDependencies(instance)
        return instance
    }

    fun <T : Any> injectDependencies(target: T) {
        val kClass = target::class

        kClass.declaredMemberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .filter { property -> property.javaField?.isAnnotationPresent(Inject::class.java) == true }
            .forEach { property ->
                val qualifier = property.annotations.filterIsInstance<Qualifier>().firstOrNull()?.annotationClass
                injectProperty(target, property, qualifier)
            }
    }

    private fun injectProperty(
        target: Any,
        property: KMutableProperty<*>,
        qualifier: KClass<out Annotation>?,
    ) {
        val instance = DIContainer.resolve(property.returnType.classifier as KClass<*>, qualifier)
        property.isAccessible = true
        property.setter.call(target, instance)
    }
}
