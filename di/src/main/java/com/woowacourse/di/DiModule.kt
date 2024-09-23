package com.woowacourse.di

import android.content.Context
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Module

class DiModule(private val applicationContext: Context, modules: List<KClass<*>>) {
    private val instances = mutableMapOf<KClass<*>, Any>()

    init {
        modules.forEach { module ->
            initInstances(module)
        }
    }

    private fun initInstances(module: KClass<*>) {
        val provider = createInstance(module)
        val functions = module.declaredFunctions

        functions.forEach { function ->
            val returnType = function.returnType.jvmErasure

            val instance =
                when {
                    function.parameters.isEmpty() -> function.call(provider)
                    else -> {
                        val params =
                            function.parameters.drop(1).map { param ->
                                val paramType = param.type.jvmErasure
                                if (param.findAnnotation<ApplicationContext>() != null) {
                                    applicationContext
                                } else {
                                    instances[paramType]
                                        ?: throw IllegalArgumentException("의존성 부족: ${paramType.simpleName}")
                                }
                            }.toTypedArray()
                        function.call(provider, *params)
                    }
                }
            instances[returnType] = instance
                ?: throw IllegalArgumentException("인스턴스를 생성하지 못했습니다: ${returnType.simpleName}")
        }
    }

    fun <T : Any> createInstance(kClass: KClass<T>): T {
        val constructor =
            kClass.constructors.firstOrNull()
                ?: throw IllegalArgumentException("${kClass.simpleName} 클래스에는 사용할 수 있는 생성자가 없습니다")

        val params =
            constructor.parameters.map { parameter ->
                val paramClass = parameter.type.jvmErasure
                instances[paramClass] ?: paramClass.instance()
            }.toTypedArray()

        val instance = constructor.call(*params)
        instances[kClass] = instance
        return instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(kClass: KClass<T>): T {
        return instances[kClass] as? T ?: createInjectInstance(kClass)
    }

    fun <T : Any> createInjectInstance(kClass: KClass<T>): T {
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("${kClass.simpleName} 클래스에는 사용할 수 있는 생성자가 없습니다")

        val params =
            constructor.parameters.map { parameter ->
                val paramClass = parameter.type.jvmErasure
                instances[paramClass] ?: paramClass.instance()
            }.toTypedArray()

        val instance = constructor.call(*params)
        instances[kClass] = instance
        return instance
    }

    fun inject(target: Any) {
        val kClass = target::class.java
        val properties = kClass.declaredFields
        properties.forEach { field ->
            if (field.isAnnotationPresent(Inject::class.java)) {
                field.isAccessible = true
                val fieldInstance = resolve(field.type.kotlin)
                field.set(target, fieldInstance)
            }
        }
    }

    private fun <T : Any> KClass<T>.instance(): T {
        val instance = this.primaryConstructor?.call() ?: this.constructors.first().call()
        instances[this] = instance
        return instance
    }

    companion object {
        private var instance: DiModule? = null

        fun setInstance(
            context: Context,
            modules: List<KClass<*>>,
        ) {
            instance = DiModule(applicationContext = context.applicationContext, modules = modules)
        }

        fun getInstance(): DiModule {
            return requireNotNull(instance) { "AppModule 인스턴스가 초기화되지 않았습니다" }
        }
    }
}
