package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import kotlin.jvm.kotlin
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

class DIContainer {
    private val instances: MutableMap<DependencyKey, Any> = mutableMapOf()
    private val bindings: MutableMap<DependencyKey, KClass<*>> = mutableMapOf()

    fun <T : Any, I : T> bind(
        type: KClass<T>,
        implementation: KClass<I>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        bindings[DependencyKey(type, qualifier)] = implementation
    }

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        instances[DependencyKey(type, qualifier)] = instance
    }

    fun <T : Any> get(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T = type.cast(resolve(DependencyKey(type, qualifier), mutableListOf()))

    private fun resolve(
        requestedKey: DependencyKey,
        resolving: MutableList<DependencyKey>,
    ): Any {
        val key = resolveKey(requestedKey)
        instances[key]?.let { instance -> return instance }

        val cycleStart = resolving.indexOf(key)
        require(cycleStart == -1) {
            val cycle = (resolving.drop(cycleStart) + key).joinToString(" → ") { it.displayName }
            "순환 의존성: $cycle"
        }

        resolving.add(key)
        try {
            val implementationType = bindings[key] ?: key.type
            val constructor =
                requireNotNull(implementationType.primaryConstructor) {
                    "${implementationType.simpleName}의 주 생성자를 찾을 수 없습니다."
                }
            val dependencies =
                constructor.parameters.map { parameter ->
                    val dependencyType =
                        requireNotNull(parameter.type.classifier as? KClass<*>) {
                            "${parameter.name}의 타입을 확인할 수 없습니다."
                        }
                    val qualifier = findQualifier(parameter.annotations)
                    resolve(DependencyKey(dependencyType, qualifier), resolving)
                }

            val instance = constructor.call(*dependencies.toTypedArray())
            injectFields(instance, resolving)
            if (instance !is ViewModel) {
                instances[key] = instance
                instances[DependencyKey(implementationType, key.qualifier)] = instance
            }
            return instance
        } finally {
            resolving.removeAt(resolving.lastIndex)
        }
    }

    private fun injectFields(
        instance: Any,
        resolving: MutableList<DependencyKey>,
    ) {
        instance.javaClass.declaredFields
            .filter { field -> field.isAnnotationPresent(Inject::class.java) }
            .forEach { field ->
                field.isAccessible = true
                val qualifier = findQualifier(field.annotations.toList())
                field.set(instance, resolve(DependencyKey(field.type.kotlin, qualifier), resolving))
            }
    }

    private fun resolveKey(requestedKey: DependencyKey): DependencyKey {
        if (requestedKey.qualifier != null) {
            require(requestedKey in bindings || requestedKey in instances) {
                "${requestedKey.displayName} 의존성이 등록되지 않았습니다."
            }
            return requestedKey
        }

        val candidates =
            (bindings.keys + instances.keys)
                .filter { key -> key.type == requestedKey.type }
                .distinct()
        require(candidates.size <= 1) {
            val qualifiers = candidates.joinToString { key -> key.qualifier?.simpleName ?: "Qualifier 없음" }
            "${requestedKey.type.simpleName} 의존성이 모호합니다. Qualifier를 지정하세요: $qualifiers"
        }
        return candidates.singleOrNull() ?: requestedKey
    }

    private fun findQualifier(annotations: List<Annotation>): KClass<out Annotation>? {
        val qualifiers =
            annotations
                .map { annotation -> annotation.annotationClass }
                .filter { annotationType -> annotationType.findAnnotation<Qualifier>() != null }
        require(qualifiers.size <= 1) { "Qualifier는 하나만 지정할 수 있습니다: $qualifiers" }
        return qualifiers.singleOrNull()
    }

    private data class DependencyKey(
        val type: KClass<*>,
        val qualifier: KClass<out Annotation>?,
    ) {
        val displayName: String
            get() = listOfNotNull(qualifier?.simpleName, type.simpleName).joinToString(" ")
    }
}
