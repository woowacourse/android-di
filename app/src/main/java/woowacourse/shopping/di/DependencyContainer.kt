package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DependencyContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()
    private val bindings =
        mutableMapOf<KClass<*>, KClass<*>>(
            CartRepository::class to DefaultCartRepository::class,
        )

    fun create(type: KClass<*>): Any {
        val objectInstance = type.objectInstance
        if (objectInstance != null) return objectInstance

        val constructor =
            type.primaryConstructor
                ?: throw IllegalArgumentException(
                    "${type.simpleName}에 primary constructor가 없습니다.",
                )
        val parameters = constructor.parameters
        val args =
            parameters.map { parameter ->
                val dependencyClass =
                    parameter.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException(
                            "${type.simpleName}의 ${parameter.name} 파라미터 타입을 확인할 수 없습니다.",
                        )

                getInstance(dependencyClass)
            }
        val instance = constructor.call(*args.toTypedArray())
        injectFields(instance)
        return instance
    }

    fun getInstance(type: KClass<*>): Any {
        val objectInstance = type.objectInstance
        if (objectInstance != null) return objectInstance

        val targetType = bindings[type] ?: type

        return instances.getOrPut(targetType) {
            create(targetType)
        }
    }

    fun injectFields(instance: Any) {
        val fields = instance.javaClass.declaredFields
        fields.forEach { field ->
            if (field.isAnnotationPresent(Inject::class.java)) {
                val dependencyType = field.type.kotlin
                val dependency = getInstance(dependencyType)
                field.isAccessible = true
                field.set(instance, dependency)
            }
        }
    }

    fun register(
        type: KClass<*>,
        instance: Any,
    ) {
        instances[type] = instance
    }
}
