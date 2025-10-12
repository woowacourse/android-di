package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object DIFactory {
    fun <T : Any> create(kClass: KClass<T>): T {
        val instance = createWithConstructor(kClass)
        injectFields(instance)
        return instance
    }

    private fun <T : Any> createWithConstructor(kClass: KClass<T>): T {
        val primaryConstructor =
            kClass.primaryConstructor
                ?: return kClass.createInstance()

        primaryConstructor.isAccessible = true

        val args =
            primaryConstructor.parameters.associateWith { param ->
                val isAnnotated = param.hasAnnotation<Inject>()
                val qualifierAnnotation =
                    param.annotations.firstOrNull { it is RoomDB || it is InMemory }?.annotationClass

                if (isAnnotated) {
                    val typeKClass =
                        param.type.classifier as? KClass<*>
                            ?: throw IllegalArgumentException("Unsupported parameter type: ${param.type}")
                    DIContainer.get(typeKClass, qualifierAnnotation)
                } else {
                    null
                }
            }
        return primaryConstructor.callBy(args.filterValues { it != null })
    }

    private fun <T : Any> injectFields(instance: T) {
        val kClass = instance::class

        kClass.memberProperties
            .filterIsInstance<KMutableProperty1<T, Any?>>()
            .filter { it.hasAnnotation<Inject>() }
            .forEach { property ->
                property.isAccessible = true

                val dependencyClass =
                    property.returnType.classifier as? KClass<*>
                        ?: throw IllegalArgumentException(
                            "Unsupported field type for injection: ${property.returnType}",
                        )

                val dependency =
                    DIContainer.get(
                        dependencyClass,
                        property.annotations.firstOrNull { it is RoomDB || it is InMemory }?.annotationClass,
                    )
                property.set(instance, dependency)
            }
    }
}
