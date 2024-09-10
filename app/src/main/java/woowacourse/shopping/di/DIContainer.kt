package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.FieldInject
import woowacourse.shopping.di.annotation.ImplementationInject
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

class DIContainer(
    diModule: DIModule,
    private val classLoader: ClassLoader,
) {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()

    init {
        diModule.injectSingletonInstance()
    }

    private fun DIModule.injectSingletonInstance() =
        singletonInstance().forEach { it.instance.putSingletonInstance(it.type) }

    private fun Any.putSingletonInstance(type: KClass<*>) {
        if (instances.containsKey(type)) {
            throw IllegalArgumentException("이미 해당 클래스의 인스턴스가 존재합니다.")
        }
        instances[type] = this
    }

    fun <T : Any> instance(classType: KClass<T>): T {
        return create(classType)
    }

    fun <T : Any> singletonInstance(classType: KClass<T>): T {
        val instance = instances[classType] ?: create(classType)
        return instance as T
    }

    private fun <T : Any> create(classType: KClass<T>): T {
        if (classType.isAbstract) {
            val subclasses = classLoader.getSubclasses(classType)
            val instanceType =
                subclasses.find { it.hasAnnotation<ImplementationInject>() }
                    ?: throw IllegalArgumentException("")
            return createImplementation(instanceType) as T
        }
        return createImplementation(classType)
    }

    private fun <T : Any> createImplementation(classType: KClass<T>): T {
        if (instances[classType] != null) return instances[classType] as T
        val parameters = classType.constructors.first().parameters
        val arguments = parameters.map(::argumentInstance)
        val instance = classType.constructors.first().call(*arguments.toTypedArray())
        instance.putSingletonInstance(classType)
        instance.injectFieldDependency()
        return instance
    }

    private fun argumentInstance(parameter: KParameter): Any {
        val parameterType = parameter.type.classifier as KClass<*>
        return instances[parameterType] ?: create(parameterType)
    }

    private fun Any.injectFieldDependency() {
        val properties =
            this::class.declaredMemberProperties
                .filter { it.isLateinit }
                .filter { it.hasAnnotation<FieldInject>() }

        properties.forEach { property ->
            val p = property as KMutableProperty1
            p.isAccessible = true
            val type = p.returnType.classifier as KClass<*>
            val instance = instances[type] ?: create(type)
            property.setter.call(this, instance)
        }
    }
}
