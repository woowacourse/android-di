package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

object DependencyInjector {
    fun <T : Any> inject(targetClass: KClass<T>): T {
        val instance = DIContainer.getInstance(targetClass)
        injectDependencies(instance)
        return instance
    }

    fun <T : Any> injectDependencies(target: T) {
        val kClass = target::class

        kClass.declaredMemberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .filter { property ->
                property.javaField?.isAnnotationPresent(Inject::class.java) == true
            }
            .forEach {
                injectProperty(target, it)
            }
    }

    private fun injectProperty(target: Any, property: KMutableProperty<*>) {
        val instance = DIContainer.getInstance(property.returnType.classifier as KClass<*>)  // DIContainer에서 인스턴스를 가져옴
        property.isAccessible = true
        property.setter.call(target, instance)
    }
}
