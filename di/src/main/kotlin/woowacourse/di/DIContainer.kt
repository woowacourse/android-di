package woowacourse.di

import kotlin.jvm.kotlin
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

class DIContainer : DIResolver {
    private val definitions: MutableMap<DependencyKey, DependencyDefinition> = mutableMapOf()
    private val singletonScopeKey: ScopeKey = ScopeKey(ScopeType.Singleton, ScopeType.Singleton.name)
    private val instanceStores: MutableMap<ScopeKey, InstanceStore> =
        mutableMapOf(singletonScopeKey to InstanceStore())
    private val singletonScope: Map<ScopeType, ScopeKey> = mapOf(ScopeType.Singleton to singletonScopeKey)

    fun <T : Any, I : T> bind(
        type: KClass<T>,
        implementation: KClass<I>,
        qualifier: KClass<out Annotation>? = null,
        scopeType: ScopeType = ScopeType.Singleton,
    ) {
        definitions[DependencyKey(type, qualifier)] = DependencyDefinition.Binding(implementation, scopeType)
    }

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        definitions[DependencyKey(type, qualifier)] = DependencyDefinition.Instance(instance)
    }

    override fun <T : Any> get(
        type: KClass<T>,
        qualifier: KClass<out Annotation>?,
    ): T = get(type, qualifier, singletonScope)

    override fun <T : Any> create(
        type: KClass<T>,
        qualifier: KClass<out Annotation>?,
    ): T = create(type, qualifier, singletonScope)

    fun openScope(
        type: ScopeType,
        id: String,
    ): ScopedContainer = openScope(type, id, singletonScope)

    fun closeScope(
        type: ScopeType,
        id: String,
    ) {
        closeScope(ScopeKey(type, id))
    }

    internal fun <T : Any> get(
        type: KClass<T>,
        qualifier: KClass<out Annotation>?,
        activeScopes: Map<ScopeType, ScopeKey>,
    ): T = type.cast(resolve(DependencyKey(type, qualifier), mutableListOf(), activeScopes))

    internal fun <T : Any> create(
        type: KClass<T>,
        qualifier: KClass<out Annotation>?,
        activeScopes: Map<ScopeType, ScopeKey>,
    ): T =
        type.cast(
            resolve(
                requestedKey = DependencyKey(type, qualifier),
                resolving = mutableListOf(),
                activeScopes = activeScopes,
                cacheResult = false,
            ),
        )

    internal fun openScope(
        type: ScopeType,
        id: String,
        parentScopes: Map<ScopeType, ScopeKey>,
    ): ScopedContainer {
        require(type != ScopeType.Singleton) { "싱글톤 스코프는 새로 열 수 없습니다." }
        require(id.isNotBlank()) { "스코프 식별자는 비어 있을 수 없습니다." }

        val scopeKey = ScopeKey(type, id)
        instanceStores.getOrPut(scopeKey, ::InstanceStore)
        return ScopedContainer(
            container = this,
            activeScopes = parentScopes + (type to scopeKey),
            ownedScope = scopeKey,
        )
    }

    internal fun closeScope(scopeKey: ScopeKey) {
        require(scopeKey.type != ScopeType.Singleton) { "싱글톤 스코프는 닫을 수 없습니다." }
        instanceStores.remove(scopeKey)?.clear()
    }

    private fun resolve(
        requestedKey: DependencyKey,
        resolving: MutableList<DependencyKey>,
        activeScopes: Map<ScopeType, ScopeKey>,
        cacheResult: Boolean = true,
    ): Any {
        val key = resolveKey(requestedKey)
        val definition = definitions[key]
        if (definition is DependencyDefinition.Instance) return definition.value

        val binding = definition as? DependencyDefinition.Binding
        val scopeType = binding?.scopeType ?: ScopeType.Singleton
        val instanceStore = findInstanceStore(scopeType, activeScopes)
        if (cacheResult) {
            instanceStore[key]?.let { instance -> return instance }
        }

        val cycleStart = resolving.indexOf(key)
        require(cycleStart == -1) {
            val cycle = (resolving.drop(cycleStart) + key).joinToString(" → ") { it.displayName }
            "순환 의존성: $cycle"
        }

        resolving.add(key)
        try {
            val implementationType = binding?.implementationType ?: key.type
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
                    resolve(DependencyKey(dependencyType, qualifier), resolving, activeScopes)
                }

            val instance = constructor.call(*dependencies.toTypedArray())
            injectFields(instance, resolving, activeScopes)
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
        activeScopes: Map<ScopeType, ScopeKey>,
    ) {
        instance.javaClass.declaredFields
            .filter { field -> field.isAnnotationPresent(Inject::class.java) }
            .forEach { field ->
                field.isAccessible = true
                val qualifier = findQualifier(field.annotations.toList())
                field.set(instance, resolve(DependencyKey(field.type.kotlin, qualifier), resolving, activeScopes))
            }
    }

    private fun findInstanceStore(
        scopeType: ScopeType,
        activeScopes: Map<ScopeType, ScopeKey>,
    ): InstanceStore {
        val scopeKey =
            requireNotNull(activeScopes[scopeType]) {
                "${scopeType.name} 스코프가 열려 있지 않습니다."
            }
        return requireNotNull(instanceStores[scopeKey]) {
            "${scopeType.name} 스코프가 이미 닫혔습니다: ${scopeKey.id}"
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
