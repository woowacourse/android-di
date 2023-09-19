package com.app.covi_di.core

import android.content.Context
import com.app.covi_di.annotation.SingletonObject
import com.app.covi_di.annotation.Inject
import com.app.covi_di.annotation.InjectField
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

object Injector {
    private const val ERROR_MODULE_NOT_CONTAINED = "Module is not contained in DIContainer"

    fun <T : Any> inject(clazz: KClass<T>, context: Context? = null): T {
        val parameterTypes = getParameterTypes(clazz, context)
        clazz.primaryConstructor?.isAccessible = true

        val instance = clazz.primaryConstructor?.call(*parameterTypes.toTypedArray()) as T
        injectFields(instance)
        return instance
    }

    private fun injectRecursive(param: KClass<*>): Any {
        if (param.primaryConstructor?.hasAnnotation<InjectField>() == true ||
            param.primaryConstructor?.hasAnnotation<Inject>() == true
        ) {
            return inject(param)
        }
        return param.createInstance()
    }

    private fun getParameterTypes(kClass: KClass<out Any>, context: Context? = null): List<Any> {
        val parameterTypes = mutableListOf<Any>()

        kClass.primaryConstructor?.valueParameters?.forEach { param ->
            val parameterType = param.type.jvmErasure
            if (parameterType.isAbstract) {
                if (DIContainer.getModuleKClass(parameterType) == null) {
                    parameterTypes.add(
                        getInstanceByProvider(parameterType) ?: context
                        ?: throw IllegalStateException()
                    )
                } else {
                    val instance = DIContainer.getModuleKClass(parameterType)
                        ?: throw IllegalArgumentException()

                    val recursiveInstance =
                        DIContainer.getSingleton(parameterType) ?: injectRecursive(instance)
                    parameterTypes.add(recursiveInstance)
                    if (instance.hasAnnotation<SingletonObject>()) {
                        DIContainer.updateSingleton(instance, recursiveInstance)
                    }
                }
            } else {
                parameterTypes.add(parameterType.createInstance())
            }
        }

        return parameterTypes
    }

    private fun getInstanceByProvider(parameterType: KClass<*>): Any? {
        return DIContainer.getProviderInstance(parameterType)
    }


    private fun injectFields(instance: Any?) {
        val instanceClass = instance?.javaClass as Class<*>
        instanceClass.declaredFields.forEach { field ->
            field.isAccessible = true
            if (field.isAnnotationPresent(InjectField::class.java)) {
                val fieldInstance: Any =
                    if (field.type.kotlin.isAbstract) DIContainer.getModuleKClass(field.type.kotlin)
                        ?: throw IllegalArgumentException(ERROR_MODULE_NOT_CONTAINED)
                    else field.type.newInstance()
                field.set(instance, fieldInstance)
            }
        }
    }
}