package com.harodi

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

data class DependencyKey(
    val classType: Class<*>,
    val qualifier: KClass<out Annotation>?,
)

class DiManager {
    // 현재는 공통 인스턴스 모음으로 사용된다.
    // 이를 스코프 단위로 인스턴스를 관리하도록 변경해야 할 것 같다.
    // 스코프 내에서 인스턴스를 찾고, 내 스코프에 없으면, 부모 스코프의 인스턴스를 탐색한다.
    private val instanceMap: MutableMap<DependencyKey, Any> = mutableMapOf()

    // 인터페이스의 경우 어떤 클래스를 구현해야할지 매핑해서 알려준다.
    private val providerMap: MutableMap<DependencyKey, Any> = mutableMapOf()

    fun addInstance(
        classType: Class<*>,
        qualifier: KClass<out Annotation>?,
        value: Any,
    ) {
        val dependencyKey = DependencyKey(classType, qualifier)
        instanceMap[dependencyKey] = value
    }

    fun addProvider(
        classType: Class<*>,
        qualifier: KClass<out Annotation>?,
        value: Any,
    ) {
        val dependencyKey = DependencyKey(classType, qualifier)
        providerMap[dependencyKey] = value
    }

    fun hasInstance(dependencyKey: DependencyKey): Boolean = instanceMap.keys.contains(dependencyKey)

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
    ): Any {
        val dependencyKey = DependencyKey(classType, qualifier)
        if (hasInstance(dependencyKey)) {
            return instanceMap[dependencyKey] ?: throw IllegalArgumentException("객체를 찾을 수 없습니다.")
        } else {
            val instance = resolve(classType, qualifier)
            instanceMap[dependencyKey] = instance
            return instance
        }
    }

    // 객체를 생성한다.
    // searchInstance와 createInstance와 서로 주고받는다.
    internal fun createInstance(dependencyKey: DependencyKey): Any {
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
                    searchInstance(type.java, qualifier)
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
                set(instance, searchInstance(dependancyKClass.java, qualifier))
            }
        }
    }

    // 전체 객체 생성 흐름을 가진다.
    // 생성자를 만들고, 생성자 주입이 필요하다면 생성자 주입을 통한 인스턴스를 생성한다.
    // 인스턴스에서 필드 주입이 존재한다면, 필드 주입도 받도록 하여 인스턴스를 반환한다.
    fun <T : Any> resolve(
        modelClass: Class<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val dependencyKey = DependencyKey(modelClass, qualifier)

        val instance = createInstance(dependencyKey)

        val lateinitProperties = searchLateinitProperty(modelClass)
        if (lateinitProperties != null) {
            fieldInject(modelClass, instance as T, lateinitProperties)
        }

        return instance as T
    }
}
