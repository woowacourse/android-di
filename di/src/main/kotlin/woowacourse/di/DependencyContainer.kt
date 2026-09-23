package woowacourse.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class DependencyContainer(
    dependencies: Map<DependencyKey, Any> = emptyMap(),
    private val bindings: Map<DependencyKey, KClass<*>> = emptyMap(),
) {
    private val dependencies: MutableMap<DependencyKey, Any> = dependencies.toMutableMap()

    fun register(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
        create: () -> Any,
    ) {
        dependencies.getOrPut(DependencyKey(type, qualifier), create)
    }

    fun create(type: KClass<*>): Any {
        val constructor = type.primaryConstructor ?: error("주 생성자를 찾을 수 없습니다: ${type.qualifiedName}")
        constructor.isAccessible = true
        val arguments =
            constructor.parameters.associateWith { parameter ->
                resolve(
                    DependencyKey(
                        type = parameter.type.jvmErasure,
                        qualifier = parameter.annotations.findQualifier(),
                    ),
                )
            }
        return constructor.callBy(arguments)
    }

    fun inject(target: Any) {
        target.javaClass.declaredFields
            .filter { it.isAnnotationPresent(Inject::class.java) }
            .forEach { field ->
                field.isAccessible = true
                field.set(
                    target,
                    resolve(
                        DependencyKey(
                            type = field.type.kotlin,
                            qualifier = field.annotations.toList().findQualifier(),
                        ),
                    ),
                )
            }
    }

    private fun resolve(requestedKey: DependencyKey): Any {
        val key = findDependencyKey(requestedKey)
        dependencies[key]?.let { return it }

        val implementation = bindings[key]
        if (implementation != null) {
            return dependencies.getOrPut(key) { create(implementation) }
        }

        if (key.qualifier != null) {
            error("등록된 의존성이 없습니다: ${key.description()}")
        }
        if (key.type.java.isInterface) {
            error("등록된 구현체가 없습니다: ${key.type.qualifiedName}")
        }
        return dependencies.getOrPut(key) { create(key.type) }
    }

    private fun findDependencyKey(requestedKey: DependencyKey): DependencyKey {
        if (requestedKey.qualifier != null) return requestedKey

        val candidates =
            (bindings.keys + dependencies.keys)
                .filter { it.type == requestedKey.type }
                .distinct()

        if (candidates.size > 1) {
            val qualifiers = candidates.mapNotNull { it.qualifier?.simpleName }.sorted()
            error(
                "${requestedKey.type.qualifiedName} 타입에 여러 의존성이 등록되어 있습니다. " +
                    "Qualifier를 지정해 주세요: ${qualifiers.joinToString()}",
            )
        }
        return candidates.singleOrNull() ?: requestedKey
    }

    private fun List<Annotation>.findQualifier(): KClass<out Annotation>? {
        val qualifiers = filter { it.annotationClass.java.isAnnotationPresent(Qualifier::class.java) }
        require(qualifiers.size <= 1) { "Qualifier는 하나만 지정할 수 있습니다." }
        return qualifiers.singleOrNull()?.annotationClass
    }

    private fun DependencyKey.description(): String = "${type.qualifiedName} @${qualifier?.simpleName}"
}
