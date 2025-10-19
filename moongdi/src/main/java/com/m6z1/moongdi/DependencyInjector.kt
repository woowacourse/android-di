package com.m6z1.moongdi

import com.m6z1.moongdi.annotation.InjectClass
import com.m6z1.moongdi.annotation.InjectField
import com.m6z1.moongdi.annotation.Single
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter
import kotlin.reflect.jvm.jvmErasure

object DependencyInjector {
    inline fun <reified T : Any> inject(): T = inject(T::class) as T

    fun inject(clazz: KClass<*>): Any {
        val constructor =
            clazz.primaryConstructor
                ?: clazz.constructors.maxByOrNull { it.parameters.size }
                ?: throw IllegalStateException("해당 생성자를 찾을 수 없음: ${clazz.simpleName}")

        if (clazz.hasAnnotation<Single>()) {
            val cached =
                try {
                    DependencyContainer.provide(clazz.java)
                } catch (e: IllegalStateException) {
                    null
                }

            cached?.let { return it }
        }

        val instance =
            if (clazz.hasAnnotation<InjectClass>()) {
                val args: Map<KParameter, Any?> =
                    constructor.parameters.associateWith { param ->
                        val paramClass = param.type.jvmErasure

                        if (paramClass.isAbstract || paramClass.java.isInterface) {
                            DependencyContainer.provide(paramClass.java)
                        } else {
                            paramClass.instantiate()
                        }
                    }

                constructor.callBy(args).apply {
                    injectField(this)
                }
            } else {
                if (constructor.parameters.isEmpty()) {
                    constructor.call()
                } else {
                    val args =
                        constructor.parameters.associateWith { param ->
                            DependencyContainer.provide(param.type.jvmErasure.java)
                        }
                    constructor.callBy(args)
                }
            }

        if (clazz.hasAnnotation<Single>()) {
            DependencyContainer.cacheSingleton(clazz, instance)
        }

        return instance
    }

    inline fun <reified T : Any> injectField(target: T) {
        val fields =
            target::class
                .declaredMemberProperties
                .filter { it.hasAnnotation<InjectField>() }

        if (fields.isEmpty()) return

        fields.forEach { field ->
            val fieldClass =
                field.returnType.classifier as? KClass<*> ?: return@forEach

            val instance =
                if (fieldClass.isAbstract || fieldClass.java.isInterface) {
                    DependencyContainer.provide(fieldClass.java)
                } else {
                    fieldClass.instantiate()
                }

            val javaField =
                field.javaField ?: field.javaGetter?.let { getter ->
                    target::class.java.getDeclaredField(field.name)
                } ?: return@forEach

            javaField.isAccessible = true
            javaField.set(target, instance)
        }
    }
}
