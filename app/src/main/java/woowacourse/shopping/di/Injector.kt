package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible

object Injector {
    fun <T> inject(clazz: Class<*>): T {
        val kClass = clazz.kotlin

        val parameterTypes = getParameterTypes(kClass)
        kClass.primaryConstructor?.isAccessible = true
        return kClass.primaryConstructor?.call(*parameterTypes.toTypedArray()) as T
    }

    private fun getParameterTypes(kClass: KClass<out Any>): MutableList<Any> {
        val parameterTypes = mutableListOf<Any>()
        kClass.primaryConstructor?.valueParameters?.forEach { param ->
            val parameterType: KClass<*> = param.type.classifier as KClass<*>
            if (parameterType.isAbstract) {
                val instance = DIContainer.get(parameterType)
                parameterTypes.add(instance)
            } else {
                parameterTypes.add(parameterType.createInstance())
            }
        }
        return parameterTypes
    }
}