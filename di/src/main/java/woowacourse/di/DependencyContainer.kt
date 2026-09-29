package woowacourse.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DependencyContainer {
    private data class DependencyKey(
        val type: KClass<*>,
        val qualifier: KClass<out Annotation>?,
    )

    private val instances = mutableMapOf<DependencyKey, Any>()

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

                val qualifier = findQualifier(parameter.annotations)

                getInstance(
                    type = dependencyClass,
                    qualifier = qualifier,
                )
            }
        val instance = constructor.call(*args.toTypedArray())
        injectFields(instance)
        return instance
    }

    fun getInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any {
        val key = DependencyKey(type, qualifier)

        instances[key]?.let { return it }

        val qualifiedInstances =
            instances.keys.filter { dependencyKey ->
                dependencyKey.type == type && dependencyKey.qualifier != null
            }

        if (qualifier == null && qualifiedInstances.isNotEmpty()) {
            throw IllegalArgumentException(
                "${type.simpleName} 타입에는 Qualifier가 필요합니다.",
            )
        }

        if (qualifier != null) {
            throw IllegalArgumentException(
                "${type.simpleName} 타입에 ${qualifier.simpleName} Qualifier가 등록되지 않았습니다.",
            )
        }

        val objectInstance = type.objectInstance
        if (objectInstance != null) return objectInstance

        return instances.getOrPut(key) {
            create(type)
        }
    }

    fun injectFields(instance: Any) {
        val fields = instance.javaClass.declaredFields
        fields.forEach { field ->
            if (!field.isAnnotationPresent(Inject::class.java)) return@forEach

            val qualifier =
                field.annotations
                    .map { it.annotationClass.java }
                    .firstOrNull { annotationClass ->
                        annotationClass.isAnnotationPresent(Qualifier::class.java)
                    }?.kotlin

            val dependency =
                getInstance(
                    type = field.type.kotlin,
                    qualifier = qualifier,
                )

            field.isAccessible = true
            field.set(instance, dependency)
        }
    }

    fun register(
        type: KClass<*>,
        instance: Any,
    ) {
        register(type, null, instance)
    }

    fun register(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
        instance: Any,
    ) {
        instances[DependencyKey(type, qualifier)] = instance
    }

    private fun findQualifier(annotations: Iterable<Annotation>): KClass<out Annotation>? {
        val qualifiers =
            annotations.filter { annotation ->
                annotation.annotationClass.java
                    .isAnnotationPresent(Qualifier::class.java)
            }

        if (qualifiers.size > 1) {
            throw IllegalArgumentException("하나의 필드 또는 파라미터에는 Qualifier를 하나만 지정해야 합니다.")
        }

        return qualifiers.firstOrNull()?.annotationClass
    }
}
