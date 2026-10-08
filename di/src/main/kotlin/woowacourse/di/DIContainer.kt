package woowacourse.di

import kotlin.jvm.kotlin
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

class DIContainer {
    private val definitions: MutableMap<DependencyKey, DependencyDefinition> = mutableMapOf()
    private val instanceStore: InstanceStore = InstanceStore()

    fun <T : Any, I : T> bind(
        type: KClass<T>,
        implementation: KClass<I>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        definitions[DependencyKey(type, qualifier)] = DependencyDefinition.Binding(implementation)
    }

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        definitions[DependencyKey(type, qualifier)] = DependencyDefinition.Instance(instance)
    }

    fun <T : Any> get(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T = type.cast(resolve(DependencyKey(type, qualifier), mutableListOf()))

    fun <T : Any> create(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T = type.cast(resolve(DependencyKey(type, qualifier), mutableListOf(), cacheResult = false))

    private fun resolve(
        requestedKey: DependencyKey,
        resolving: MutableList<DependencyKey>,
        cacheResult: Boolean = true,
    ): Any {
        val key = resolveKey(requestedKey)
        if (cacheResult) {
            instanceStore[key]?.let { instance -> return instance }
        }

        val definition = definitions[key]
        if (definition is DependencyDefinition.Instance) return definition.value

        val cycleStart = resolving.indexOf(key)
        require(cycleStart == -1) {
            val cycle = (resolving.drop(cycleStart) + key).joinToString(" → ") { it.displayName }
            "순환 의존성: $cycle"
        }

        resolving.add(key)
        try {
            val implementationType =
                when (definition) {
                    is DependencyDefinition.Binding -> definition.implementationType
                    is DependencyDefinition.Instance -> error("등록된 인스턴스는 객체 생성 과정에 진입하지 않습니다.")
                    null -> key.type
                }
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
            if (cacheResult) {
                instanceStore[key] = instance
                instanceStore[DependencyKey(implementationType, key.qualifier)] = instance
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
            require(requestedKey in definitions) {
                "${requestedKey.displayName} 의존성이 등록되지 않았습니다."
            }
            return requestedKey
        }

        val candidates =
            definitions.keys
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
}
