package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object DIFactory {
    fun <T : Any> create(kClass: KClass<T>): T {
        val primaryConstructor = kClass.primaryConstructor

        if (primaryConstructor == null) {
            return kClass.createInstance()
        }

        primaryConstructor.isAccessible = true

        return if (primaryConstructor.parameters.isEmpty()) {
            primaryConstructor.call()
        } else {
            val args =
                primaryConstructor.parameters.associateWith { param ->
                    val typeKClass =
                        param.type.classifier as? KClass<*>
                            ?: throw IllegalArgumentException("Unsupported parameter type: ${param.type}")
                    DIContainer.get(typeKClass)
                }
            primaryConstructor.callBy(args)
        }
    }
}
