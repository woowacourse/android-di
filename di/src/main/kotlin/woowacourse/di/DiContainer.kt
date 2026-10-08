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
    private val scopes = mutableMapOf<String, Scope>()

    fun openScope(scopeId: String): Scope = scopes.getOrPut(scopeId) { Scope() }

    fun closeScope(scopeId: String) {
        scopes.remove(scopeId)
    }

    // 외부에는 스코프의 식별 가능한 핸들만 반환하고, 객체 보관 Map은 노출하지 않는다.
    // 현재 단위에서는 보관함의 수명만 관리하며 생성과 주입 연결은 다음 단위에서 수행한다.
    class Scope internal constructor() {
        private val store = mutableMapOf<DiKey, Any>()
    }

    fun <T : Any> instantiate(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val key = resolveKey(type, qualifier)
        store[key]?.let { return type.cast(it) }

        val implementationType = interfaceRules[key] ?: type
        val constructor =
            requireNotNull(implementationType.primaryConstructor) {
                "생성자가 없습니다."
            }

        val dependencies =
            constructor.parameters.map { parameter ->
                val dependencyType = parameter.type.classifier as KClass<*>
                val dependencyQualifier = qualifierOf(parameter.annotations, parameter.name.orEmpty())
                instantiate(dependencyType, dependencyQualifier)
            }

        val instance = type.cast(constructor.call(*dependencies.toTypedArray()))
        store[key] = instance
        return instance
    }

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        store[DiKey(type, qualifier)] = instance
    }

    fun <T : Any, I : T> registerInterfaceRule(
        type: KClass<T>,
        implementationType: KClass<I>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        interfaceRules[DiKey(type, qualifier)] = implementationType
    }

    fun inject(target: Any) {
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
            val dependency = instantiate(dependencyType, dependencyQualifier)

            mutableProperty.isAccessible = true
            mutableProperty.setter.call(target, dependency)
        }
    }

    private fun resolveKey(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ): DiKey {
        val requestedKey = DiKey(type, qualifier)
        if (store.containsKey(requestedKey) || interfaceRules.containsKey(requestedKey)) {
            return requestedKey
        }

        require(qualifier == null) {
            "등록되지 않은 Qualifier입니다. " +
                "요청 타입: ${type.qualifiedName}, " +
                "Qualifier: ${qualifier?.qualifiedName}"
        }

        val candidates =
            (store.keys + interfaceRules.keys)
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

    private data class DiKey(
        // 코어는 앱 애노테이션의 의미를 해석하지 않고 타입과 함께 식별자로만 사용한다.
        val type: KClass<*>,
        val qualifier: KClass<out Annotation>?,
    )
}
