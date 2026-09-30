package woowacourse.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.cast
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

class DependencyContainer {
    private val instances: MutableMap<DependencyKey, Any> = mutableMapOf()

    fun <T : Any> resolve(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val instance =
            if (qualifier == null) {
                resolveWithoutQualifier(type)
            } else {
                val key = DependencyKey(type, qualifier)
                instances.getOrPut(key) {
                    create(type)
                }
            }

        return type.cast(instance)
    }

    private fun resolveWithoutQualifier(type: KClass<*>): Any {
        val candidates = instances.filterKeys { it.type == type }

        return when (candidates.size) {
            0 -> {
                val key = DependencyKey(type, null)
                instances.getOrPut(key) {
                    create(type)
                }
            }
            1 -> candidates.values.single()
            else -> {
                val qualifiers =
                    candidates.keys
                        .map { key ->
                            key.qualifier?.qualifiedName ?: "Qualifier 없음"
                        }.joinToString()
                throw IllegalStateException("${type.qualifiedName}에 여러 의존성이 등록되어 있습니다. 등록된 Qualifier : $qualifiers")
            }
        }
    }

    private fun create(type: KClass<*>): Any {
        val constructor =
            requireNotNull(type.primaryConstructor) {
                "${type.qualifiedName}의 주 생성자를 찾을 수 없습니다."
            }

        val dependencies =
            constructor.parameters.map { parameter ->
                val dependencyType =
                    requireNotNull(
                        parameter.type.classifier as? KClass<*>,
                    ) {
                        "${parameter.name}의 타입을 확인할 수 없습니다."
                    }

                resolve(dependencyType)
            }

        return constructor.call(*dependencies.toTypedArray())
    }

    fun inject(instance: Any) {
        instance::class
            .declaredMemberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .filter { it.findAnnotation<MyInject>() != null }
            .forEach { property ->
                val dependencyType =
                    requireNotNull(
                        property.returnType.classifier as? KClass<*>,
                    ) {
                        "${property.name}의 타입을 확인할 수 없습니다."
                    }
                val qualifier =
                    property.annotations
                        .find { annotation ->
                            annotation.annotationClass.findAnnotation<Qualifier>() != null
                        }?.annotationClass
                property.setter.call(instance, resolve(dependencyType, qualifier))
            }
    }

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        val key = DependencyKey(type, qualifier)
        instances[key] = instance
    }
}

private data class DependencyKey(
    val type: KClass<*>,
    val qualifier: KClass<out Annotation>?,
)
