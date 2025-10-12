package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object DIFactory {
    fun <T : Any> create(kClass: KClass<T>): T {
        val primaryConstructor =
            kClass.primaryConstructor
                ?: return kClass.createInstance()

        primaryConstructor.isAccessible = true

        val args =
            primaryConstructor.parameters.associateWith { param ->
                val isAnnotated = param.findAnnotation<Inject>() != null

                if (isAnnotated) {
                    val typeKClass =
                        param.type.classifier as? KClass<*>
                            ?: throw IllegalArgumentException("Unsupported parameter type: ${param.type}")
                    DIContainer.get(typeKClass)
                } else {
                    null
                }
            }

        return primaryConstructor.callBy(args.filterValues { it != null })
    }
}
