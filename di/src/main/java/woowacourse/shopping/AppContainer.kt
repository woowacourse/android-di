package woowacourse.shopping

import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.Qualifier
import woowacourse.shopping.annotation.SingleInstance
import woowacourse.shopping.dslbuilder.ProviderBuilder
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class AppContainer {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()
    private var providers: Map<KClass<*>, KFunction<*>> = emptyMap()
    private val qualifiers: MutableMap<Qualifier, KClass<*>> = mutableMapOf()

    fun providers(block: ProviderBuilder.() -> Unit) {
        providers = ProviderBuilder().apply(block).build()
    }

    fun <T : Any> addQualifier(qualifier: Qualifier, clazz: KClass<T>) {
        qualifiers[qualifier] = clazz
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> inject(clazz: Class<T>): T {
        val kClass = clazz.kotlin

        instances[kClass]?.let { return it as T }

        val instance = getInstanceOf(kClass) ?: createInstanceOf(kClass)
        injectFields(kClass, instance)

        saveInstance(kClass, instance)
        return instance
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> getInstanceOf(implementationClass: KClass<out T>): T? {
        return providers[implementationClass]?.let { provider ->
            val args = getArguments(provider)
            provider.call(*args) as T
        }
    }

    private fun getArguments(func: KFunction<*>): Array<Any> {
        val args = func.parameters.map {
            inject(it.getImplementationClass().java)
        }.toTypedArray()
        return args
    }

    private fun KParameter.getImplementationClass(): KClass<*> {
        val qualifier = findAnnotation<Qualifier>()
        return qualifier?.let {
            qualifiers[it]
        } ?: type.jvmErasure
    }

    private fun <T : Any> createInstanceOf(implementationClass: KClass<out T>): T {
        val constructor = implementationClass.primaryConstructor
            ?: throw NullPointerException("주입할 클래스의 주생성자가 존재하지 않습니다.")

        if (constructor.parameters.isEmpty()) return implementationClass.createInstance()

        val args = getArguments(constructor)
        return constructor.call(*args)
    }

    private fun <T : Any> injectFields(clazz: KClass<out T>, instance: T) {
        clazz.declaredMemberProperties.forEach {
            if (it.hasAnnotation<Inject>() && it is KMutableProperty<*>) {
                it.isAccessible = true
                it.setter.call(instance, inject(it.getImplementationClass().java))
            }
        }
    }

    private fun <T : Any> KProperty1<T, *>.getImplementationClass(): KClass<*> {
        val qualifier = findAnnotation<Qualifier>()
        return qualifier?.let {
            qualifiers[it]
        } ?: returnType.jvmErasure
    }

    private fun <T : Any> saveInstance(implementationClass: KClass<out T>, instance: T) {
        if (implementationClass.hasAnnotation<SingleInstance>()) {
            instances[implementationClass] = instance
        }
    }
}
