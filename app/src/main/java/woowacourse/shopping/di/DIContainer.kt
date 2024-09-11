package woowacourse.shopping.di

import android.util.Log
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.di.annotation.FieldInject
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

class DIContainer(
    application: ShoppingApplication,
    diModules: List<KClass<out DIModule>>,
) {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()

    init {
        instances[ShoppingApplication::class] = application
        diModules.filter { !it.isAbstract }.forEach { it.injectModule() }
        diModules.filter { it.isAbstract }.forEach { it.injectAbstractModule() }
    }

    private fun KClass<out DIModule>.injectAbstractModule() {
        val functions = this.declaredMemberFunctions
        functions.forEach { function ->
            val instanceType = function.parameters
                .map { it.type.classifier as KClass<*> }
                .first { it != this }

            val instance = create(instanceType)
            val type = function.returnType.classifier as KClass<*>
            instances[type] = instance
        }
    }

    private fun KClass<out DIModule>.injectModule() {
        val functions = this.declaredMemberFunctions
        functions.forEach { function ->
            val type = function.returnType.classifier as KClass<*>
            val parameters = function.parameters
                .map { it.type.classifier as KClass<*> }
                .filter { this != it }
            val objectInstance = create(this)
            val arguments = parameters.map { singletonInstance(it) }
            val instance = function.call(objectInstance, *arguments.toTypedArray())
            instances[type] = instance ?: throw IllegalArgumentException()
        }
    }

    fun <T : Any> instance(classType: KClass<T>): T {
        return create(classType)
    }

    fun <T : Any> singletonInstance(classType: KClass<T>): T {
        return instances[classType] as? T ?: create(classType).apply {
            putSingletonInstance(classType)
        }
    }

    private fun <T : Any> create(classType: KClass<T>): T {
        if (instances[classType] != null) return instances[classType] as T
        Log.e("TEST", "create ${classType.simpleName}, ${classType.constructors.size}")
        val parameters = classType.constructors.first().parameters
        val arguments = parameters.map(::argumentInstance)
        val instance = classType.constructors.first().call(*arguments.toTypedArray())
        instance.injectFieldDependency()
        return instance
    }

    private fun argumentInstance(parameter: KParameter): Any {
        val parameterType = parameter.type.classifier as KClass<*>
        return instances[parameterType] ?: create(parameterType)
    }

    private fun Any.putSingletonInstance(type: KClass<*>) {
        if (instances.containsKey(type)) {
            throw IllegalArgumentException("이미 해당 클래스의 인스턴스가 존재합니다.")
        }
        instances[type] = this
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
