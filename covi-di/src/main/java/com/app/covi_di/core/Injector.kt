package com.app.covi_di.core

import com.app.covi_di.annotation.Inject
import com.app.covi_di.annotation.InjectField
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible

object Injector {
    fun <T> inject(clazz: KClass<*>): T {
        val parameterTypes = getParameterTypes(clazz)
        clazz.primaryConstructor?.isAccessible = true
        val instance = clazz.primaryConstructor?.call(*parameterTypes.toTypedArray())
        injectFields(instance)
        return instance as T
    }

    private fun injectRecursive(param: KClass<*>): Any {
        if (param.primaryConstructor?.hasAnnotation<InjectField>() == true ||
            param.primaryConstructor?.hasAnnotation<Inject>() == true
        ) {
            return inject(param)
        }
        return param.createInstance()
    }

    private fun getParameterTypes(kClass: KClass<out Any>): MutableList<Any> {
        val parameterTypes = mutableListOf<Any>()

        kClass.primaryConstructor?.valueParameters?.forEach { param ->
            val parameterType: KClass<*> = param.type.classifier as KClass<*>
            if (parameterType.isAbstract) {
                if (DIContainer.getModuleKClass(parameterType) == null) {
                    parameterTypes.add(getByProvider(parameterType))
                } else {
                    val instance = DIContainer.getModuleKClass(parameterType)
                        ?: throw IllegalArgumentException()
                    val recursiveInstance = injectRecursive(instance)
                    parameterTypes.add(recursiveInstance)
                }
            } else {
                parameterTypes.add(parameterType.createInstance())
            }
        }

        return parameterTypes
    }

    private fun getByProvider(parameterType: KClass<*>): Any {
        return DIContainer.getProviderInstance(parameterType)
    }


    private fun injectFields(instance: Any?) {
        val instanceClass = instance?.javaClass as Class<*>
        instanceClass.declaredFields.forEach { field ->
            field.isAccessible = true
            if (field.isAnnotationPresent(InjectField::class.java)) {
                val fieldInstance: Any =
                    if (field.type.kotlin.isAbstract) DIContainer.getModuleKClass(field.type.kotlin)
                        ?: throw IllegalArgumentException()
                    else field.type.newInstance()
                field.set(instance, fieldInstance)
            }
        }
    }
}