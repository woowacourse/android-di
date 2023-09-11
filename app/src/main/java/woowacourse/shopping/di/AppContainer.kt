package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class AppContainer {
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

        val instance = getInstanceOf(implementationClass)
        if (instance != null) return instance as T

        return createInstanceOf(implementationClass) as T
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
}
