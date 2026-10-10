package com.harodi

import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

data class DependencyKey(
    val classType: Class<*>,
    val qualifier: KClass<out Annotation>?,
)

// 의존성이 추가되야 할 영역의 종류를 구분하기 위한 인터페이스
interface ScopeKind

data class ScopeKey(
    val parentKey: ScopeKey?,
    val uuid: UUID = UUID.randomUUID(),
    val scopeKind: ScopeKind,
)

class DiManager {
    // 스코프의 관계를 나타내기 위한 Map이다.
    private val scopeMap: MutableMap<ScopeKey, MutableMap<DependencyKey, Any>> = mutableMapOf()

    // 해당 클래스가 어떤 스코프에 해당하는지 규칙을 관리하는 Map이다.
    private val scopePolicyMap: MutableMap<DependencyKey, ScopeKind> = mutableMapOf()

    // 인터페이스의 경우 어떤 클래스를 구현해야할지 매핑해서 알려준다.
    private val providerMap: MutableMap<DependencyKey, Any> = mutableMapOf()

    // 의존성 주입을 받아야하는 클래스에 대한 스코프 범위를 정의해주는 역할을 하는 함수다.
    fun addScopePolicy(
        classType: Class<*>,
        qualifier: KClass<out Annotation>?,
        scopeKind: ScopeKind,
    ) {
        val dependencyKey = DependencyKey(classType, qualifier)
        scopePolicyMap[dependencyKey] = scopeKind
    }

    fun searchScopePolicy(
        classType: Class<*>,
        qualifier: KClass<out Annotation>?,
    ): ScopeKind {
        val dependencyKey = DependencyKey(classType, qualifier)
        return scopePolicyMap[dependencyKey]
            ?: throw IllegalArgumentException("스코프 범위를 가진 Key를 찾을 수 없습니다: $classType, $qualifier")
    }

    // 인스턴스를 생성할 때 특정 스코프에 저장하여 관리하기 위한 함수다.
    fun addScope(
        scopeKey: ScopeKey,
        classType: Class<*>,
        qualifier: KClass<out Annotation>?,
        value: Any,
    ) {
        val instanceMap =
            scopeMap.getOrPut(scopeKey) {
                mutableMapOf()
            }
        val dependencyKey = DependencyKey(classType, qualifier)
        instanceMap.getOrPut(
            dependencyKey,
        ) {
            value
        }
    }

    // 특정한 스코프 범위가 종료되었을 때 해당 스코프의 인스턴스들을 제거하기 위한 함수다.
    fun removeScope(scopeKey: ScopeKey) {
        scopeMap.remove(scopeKey)
    }

    fun searchScope(
        scopeKey: ScopeKey,
        dependencyKey: DependencyKey,
    ): Any? = scopeMap[scopeKey]?.get(dependencyKey)

    fun addProvider(
        classType: Class<*>,
        qualifier: KClass<out Annotation>?,
        value: Any,
    ) {
        val dependencyKey = DependencyKey(classType, qualifier)
        providerMap[dependencyKey] = value
    }

    // 인터페이스를 받았을 때 구현할 구현체의 클래스가 무엇인지 조건을 구분한다.
    // 만약 providerMap에 없으면 modelClass를 반환한다.
    internal fun filterModelClass(dependencyKey: DependencyKey): Class<*> {
        // 1. 타입과 Qualifier가 정확히 일치하는 binding
        val exactProvider = providerMap[dependencyKey]

        if (exactProvider != null) {
            return exactProvider as Class<*>
        }

        // 2. 같은 타입으로 등록된 모든 후보
        val candidates =
            providerMap.filterKeys { registeredKey ->
                registeredKey.classType == dependencyKey.classType
            }

        // 3. Qualifier를 지정했지만 정확히 일치하는 binding이 없음
        if (dependencyKey.qualifier != null) {
            throw IllegalArgumentException(
                "${dependencyKey.classType.simpleName}에 " +
                    "${dependencyKey.qualifier.simpleName}으로 등록된 구현체가 없습니다.",
            )
        }

        // 4. Qualifier가 없고 후보가 여러 개
        if (candidates.size > 1) {
            throw IllegalArgumentException(
                "${dependencyKey.classType.simpleName}에 " +
                    "여러 구현체가 등록되어 있습니다. Qualifier를 지정해주세요.",
            )
        }

        // 5. Qualifier가 없고 후보가 하나
        if (candidates.size == 1) {
            return candidates.values.single() as Class<*>
        }

        // 6. provider가 없다면 구체 클래스 자체 생성 시도
        return dependencyKey.classType
    }

    // 객체를 탐색한다.
    // 지금은 생성까지 하고 있따. 역할을 분리할 필요가 있따.
    internal fun searchInstance(
        classType: Class<*>,
        qualifier: KClass<out Annotation>?,
        scopeKey: ScopeKey,
    ): Any {
        val dependencyKey = DependencyKey(classType, qualifier)
        val scopeInstance = searchScope(scopeKey, dependencyKey)
        if (scopeInstance != null) {
            return scopeInstance
        } else {
            val instance = resolve(classType, scopeKey, qualifier)
            addScope(scopeKey, classType, qualifier, instance)
            return instance
        }
    }

    // 객체를 생성한다.
    // searchInstance와 createInstance와 서로 주고받는다.
    internal fun createInstance(
        dependencyKey: DependencyKey,
        scopeKey: ScopeKey,
    ): Any {
        val modelClass = filterModelClass(dependencyKey)
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: throw IllegalArgumentException("생성자를 찾을 수 없습니다 : $modelClass")
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }
        if (types.isEmpty()) { // 파라미터가 없으면 그냥 생성한다.
            return constructor.call()
        } else { // 그게 아니라면 다시 탐색해서 객체를 찾아온다.
            val typesConstructors =
                types.map { type ->
                    val qualifier =
                        type.annotations
                            .firstOrNull { annotation ->
                                annotation.annotationClass.annotations.any {
                                    it is Qualifier
                                }
                            }?.annotationClass
                    val targetScopeKind = searchScopePolicy(type.java, qualifier)
                    val ownerScopeKey = findOwnerScopeKey(targetScopeKind, scopeKey)!!
                    searchInstance(type.java, qualifier, ownerScopeKey)
                }
            return constructor.call(*typesConstructors.toTypedArray())
        }
    }

    internal fun <T : Any> searchLateinitProperty(modelClass: Class<T>): List<KProperty1<T, *>>? {
        val lateinitProperties =
            modelClass.kotlin.memberProperties.filter { property ->
                property.isLateinit && property.annotations.any { it is Inject }
            }

        return lateinitProperties.ifEmpty { null }
    }

    internal fun <T : Any> fieldInject(
        modelClass: Class<T>,
        instance: T,
        lateinitProperties: List<KProperty1<T, *>>,
        scopeKey: ScopeKey,
    ) {
        lateinitProperties.forEach {
            modelClass.getDeclaredField(it.name).apply {
                val dependancyKClass =
                    it.returnType.classifier as? KClass<*>
                        ?: throw IllegalArgumentException("프로퍼티 타입을 찾을 수 없어요: $it")
                val qualifier =
                    it.annotations
                        .firstOrNull { annotation ->
                            annotation.annotationClass.annotations.any {
                                it is Qualifier
                            }
                        }?.annotationClass
                isAccessible = true
                val targetScopeKind = searchScopePolicy(dependancyKClass.java, qualifier)
                val ownerScopeKey = findOwnerScopeKey(targetScopeKind, scopeKey)!!
                set(instance, searchInstance(dependancyKClass.java, qualifier, ownerScopeKey))
            }
        }
    }

    fun findOwnerScopeKey(
        targetScopeKind: ScopeKind,
        scopeKey: ScopeKey,
    ): ScopeKey? {
        var currentScopeKey = scopeKey
        while (currentScopeKey.scopeKind != targetScopeKind) {
            currentScopeKey = currentScopeKey.parentKey ?: return null
        }
        return currentScopeKey
    }

    // 생성할 인스턴스의 타입과 qualifier, scope의 종류를 받는다.
    // 스코프 객체를 생성한다.
    // 스코프 객체에 인스턴스를 저장한다.
    fun <T : Any> resolve(
        modelClass: Class<T>,
        scopeKey: ScopeKey,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val dependencyKey = DependencyKey(modelClass, qualifier)
        val instance = createInstance(dependencyKey, scopeKey)

        val lateinitProperties = searchLateinitProperty(modelClass)
        if (lateinitProperties != null) {
            fieldInject(modelClass, instance as T, lateinitProperties, scopeKey)
        }

        return instance as T
    }
}
