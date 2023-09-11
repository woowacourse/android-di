package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.annotation.InjectField
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible

object Injector {
    fun <T> inject(clazz: Class<*>): T {
        val kClass = clazz.kotlin
        val parameterTypes = getParameterTypes(kClass)
        kClass.primaryConstructor?.isAccessible = true
        val instance = kClass.primaryConstructor?.call(*parameterTypes.toTypedArray())
        injectFields(instance)
        return instance as T
    }

    private fun getParameterTypes(kClass: KClass<out Any>): MutableList<Any> {
        val parameterTypes = mutableListOf<Any>()
        if (kClass.primaryConstructor?.hasAnnotation<Inject>() == false) {
            return parameterTypes
        }
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

    private fun injectFields(instance: Any?) {
        val instanceClass = instance?.javaClass as Class<*>
        instanceClass.declaredFields.forEach { field ->
            field.isAccessible = true
            if (field.isAnnotationPresent(InjectField::class.java)) {
                val fieldInstance: Any =
                    if (field.type.kotlin.isAbstract) DIContainer.get(field.type.kotlin)
                    else field.type.newInstance()
                field.set(instance, fieldInstance)
            }
        }
    }
}