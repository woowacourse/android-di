package woowacourse.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.cast
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class DiContainer {
    private val store = mutableMapOf<DiKey, Any>()
    private val interfaceRules = mutableMapOf<DiKey, KClass<*>>()
    private val scopeRules = mutableMapOf<DiKey, ScopeType>()
    private val scopes = mutableMapOf<String, Scope>()

    fun openScope(scopeId: String): Scope = scopes.getOrPut(scopeId) { Scope() }

    fun closeScope(scopeId: String) {
        // 호출자가 Scope 핸들을 보관해도 종료된 보관함이 객체 참조를 붙잡지 않도록 비운다.
        scopes.remove(scopeId)?.store?.clear()
    }

    // 외부에는 스코프의 식별 가능한 핸들만 반환하고, 객체 보관 Map은 노출하지 않는다.
    class Scope internal constructor() {
        internal val store = mutableMapOf<DiKey, Any>()
    }

    fun <T : Any> instantiate(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        scopeId: String? = null,
    ): T = instantiateUsing(type, qualifier) { scopeId }

    fun <T : Any> instantiate(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        scopeContext: ScopeContext,
    ): T = instantiateUsing(type, qualifier) { key -> selectScopeId(key, scopeContext) }

    private fun <T : Any> instantiateUsing(
        type: KClass<T>,
        qualifier: KClass<out Annotation>?,
        scopeIdFor: (DiKey) -> String?,
    ): T {
        val key = resolveKey(type, qualifier)
        val scopeId = scopeIdFor(key)
        val targetStore = selectStore(scopeId)
        targetStore[key]?.let { return type.cast(it) }

        val implementationType = interfaceRules[key] ?: type
        val constructor =
            requireNotNull(implementationType.primaryConstructor) {
                "생성자가 없습니다."
            }

        val dependencies =
            constructor.parameters.map { parameter ->
                val dependencyType = parameter.type.classifier as KClass<*>
                val dependencyQualifier = qualifierOf(parameter.annotations, parameter.name.orEmpty())
                instantiateUsing(dependencyType, dependencyQualifier, scopeIdFor)
            }

        val instance = type.cast(constructor.call(*dependencies.toTypedArray()))
        targetStore[key] = instance
        return instance
    }

    private fun selectScopeId(
        key: DiKey,
        scopeContext: ScopeContext,
    ): String {
        val scopeType =
            requireNotNull(scopeRules[key] ?: scopeRules[DiKey(key.type, null)]) {
                "스코프 규칙이 없습니다. " +
                    "요청 타입: ${key.type.qualifiedName}, " +
                    "Qualifier: ${key.qualifier?.qualifiedName}"
            }
        return requireNotNull(scopeContext.scopeIds[scopeType]) {
            "현재 생성 문맥에 스코프 ID가 없습니다: ${scopeType.name}"
        }
    }

    private fun selectStore(scopeId: String?): MutableMap<DiKey, Any> =
        if (scopeId == null) {
            store
        } else {
            requireNotNull(scopes[scopeId]) { "열리지 않은 스코프입니다: $scopeId" }.store
        }

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        store[DiKey(type, qualifier)] = instance
    }

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
        scopeContext: ScopeContext,
    ) {
        val key = DiKey(type, qualifier)
        val scopeId = selectScopeId(key, scopeContext)
        selectStore(scopeId)[key] = instance
    }

    fun <T : Any, I : T> registerInterfaceRule(
        type: KClass<T>,
        implementationType: KClass<I>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        interfaceRules[DiKey(type, qualifier)] = implementationType
    }

    fun <T : Any> registerScopeRule(
        type: KClass<T>,
        scopeType: ScopeType,
        qualifier: KClass<out Annotation>? = null,
    ) {
        scopeRules[DiKey(type, qualifier)] = scopeType
    }

    fun inject(target: Any) = injectUsing(target) { type, qualifier -> instantiate(type, qualifier) }

    fun inject(
        target: Any,
        scopeContext: ScopeContext,
    ) = injectUsing(target) { type, qualifier -> instantiate(type, qualifier, scopeContext) }

    private fun injectUsing(
        target: Any,
        dependencyFor: (KClass<*>, KClass<out Annotation>?) -> Any,
    ) {
        val annotatedProperties =
            target::class
                .declaredMemberProperties
                .filter { property -> property.annotations.any { it is FieldInject } }

        annotatedProperties.forEach { property ->
            val mutableProperty =
                property as? KMutableProperty1<*, *>
                    ?: error("주입 대상은 var여야 합니다")
            val dependencyType =
                mutableProperty.returnType.classifier as? KClass<*>
                    ?: error("주입 대상의 타입을 확인할 수 없습니다: ${property.name}")
            val dependencyQualifier = qualifierOf(property.annotations, property.name)
            val dependency = dependencyFor(dependencyType, dependencyQualifier)

            mutableProperty.isAccessible = true
            mutableProperty.setter.call(target, dependency)
        }
    }

    private fun resolveKey(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ): DiKey {
        val requestedKey = DiKey(type, qualifier)
        val registeredKeys = store.keys + scopes.values.flatMap { scope -> scope.store.keys }
        if (requestedKey in registeredKeys || interfaceRules.containsKey(requestedKey)) {
            return requestedKey
        }

        require(qualifier == null) {
            "등록되지 않은 Qualifier입니다. " +
                "요청 타입: ${type.qualifiedName}, " +
                "Qualifier: ${qualifier?.qualifiedName}"
        }

        val candidates =
            (registeredKeys + interfaceRules.keys)
                .filter { key -> key.type == type }
                .distinct()

        // 후보가 하나뿐이면 기존의 타입 기반 요청을 유지하고, 둘 이상일 때만 선택 정보를 요구한다.
        return when (candidates.size) {
            0 -> requestedKey
            1 -> candidates.single()
            else ->
                error(
                    "구현체에 대한 구분자가 없습니다. " +
                        "요청 타입: ${type.qualifiedName}, " +
                        "후보: ${candidates.map { it.qualifier?.qualifiedName }}",
                )
        }
    }

    private fun qualifierOf(
        annotations: List<Annotation>,
        injectionPoint: String,
    ): KClass<out Annotation>? {
        val qualifiers =
            annotations
                .map { annotation -> annotation.annotationClass }
                .filter { annotationType -> annotationType.annotations.any { it is Qualifier } }

        require(qualifiers.size <= 1) {
            "Qualifier는 하나만 지정할 수 있습니다: $injectionPoint"
        }
        return qualifiers.singleOrNull()
    }

    internal data class DiKey(
        // 코어는 앱 애노테이션의 의미를 해석하지 않고 타입과 함께 식별자로만 사용한다.
        val type: KClass<*>,
        val qualifier: KClass<out Annotation>?,
    )
}
