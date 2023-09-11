package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class AppContainer {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()

    private val providers: MutableMap<KClass<*>, KFunction<*>> = mutableMapOf()

    private val implementationClasses: MutableMap<KClass<*>, KClass<*>> = mutableMapOf()

    fun <T : Any> addProvider(clazz: KClass<T>, provider: KFunction<T>) {
        providers[clazz] = provider
    }

    fun <T : Any> addImplementationClass(abstract: KClass<T>, implement: KClass<out T>) {
        implementationClasses[abstract] = implement
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> inject(clazz: Class<T>): T {
        val implementationClass =
            implementationClasses[clazz.kotlin] ?: clazz.kotlin as KClass<out T>

        instances[implementationClass]?.let { return it as T }

        val instance = getInstanceOf(implementationClass) ?: createInstanceOf(implementationClass)
        injectFields(implementationClass, instance)

        saveInstance(implementationClass, instance)
        return instance as T
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> getInstanceOf(implementationClass: KClass<out T>): T? {
        return providers[implementationClass]?.let {
            it.call() as T
        }
    }

    private fun <T : Any> createInstanceOf(implementationClass: KClass<out T>): T {
        val constructor = implementationClass.primaryConstructor
            ?: throw NullPointerException("주입할 클래스의 주생성자가 존재하지 않습니다.")
        val parameters = constructor.parameters

        if (parameters.isEmpty()) return implementationClass.createInstance()

        val args = parameters.map {
            inject(it.type.jvmErasure.java)
        }.toTypedArray()
        return constructor.call(*args)
    }

    private fun <T : Any> injectFields(clazz: KClass<out T>, instance: T) {
        clazz.declaredMemberProperties.forEach {
            if (it.hasAnnotation<Inject>() && it is KMutableProperty<*>) {
                it.isAccessible = true
                it.setter.call(instance, inject(it.returnType.jvmErasure.java))
            }
        }
    }

    private fun <T : Any> saveInstance(implementationClass: KClass<out T>, instance: T) {
        if (implementationClass.hasAnnotation<SingleInstance>()) {
            instances[implementationClass] = instance
        }
    }
}
