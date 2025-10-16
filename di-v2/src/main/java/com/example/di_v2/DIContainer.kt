package com.example.di_v2

import com.example.di_v2.annotation.Inject
import com.example.di_v2.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@Suppress("UNCHECKED_CAST")
class DIContainer {
    private val bindings = mutableMapOf<KClass<*>, MutableMap<KClass<*>?, () -> Any>>()

    fun <T : Any> resolve(
        type: KClass<T>,
        qualifier: KClass<*>? = null,
    ): T = getInstance(type, qualifier)

    fun <T : Any> register(
        type: KClass<T>,
        qualifier: KClass<*>? = null,
        factory: () -> T,
    ) {
        val qualifierMap = bindings.getOrPut(type) { mutableMapOf() }
        qualifierMap[qualifier] = factory
    }

    fun inject(instance: Any) {
        val kClass = instance::class

        // 해당 클래스에 있는 모든 프로퍼티를 가져옴
        kClass.memberProperties.forEach { injectedProperty ->
            // @Inject가 붙은 mutable property만 주입
            // KMutableProperty1: 프로퍼티의 값을 변경할 수 있는 프로퍼티
            if (injectedProperty.annotations.any { it.annotationClass == Inject::class } &&
                injectedProperty is KMutableProperty1
            ) {
                val qualifier = findQualifier(injectedProperty.annotations)
                val dependencyType =
                    injectedProperty.returnType.classifier as? KClass<*>
                        ?: throw IllegalStateException("Cannot resolve type for property: ${injectedProperty.name}")
                val dependency = getInstance(dependencyType as KClass<Any>, qualifier)

                // isAccessible: 프로퍼티의 접근 지시자를 변경할 수 있는 속성
                injectedProperty.isAccessible = true
                // setter.call: 프로퍼티의 setter를 호출하여 값을 설정
                injectedProperty.setter.call(instance, dependency)
            }
        }
    }

    fun printBindings() {
        println("=== DIContainer Bindings ===")
        bindings.forEach { (type, map) ->
            map.forEach { (qualifier, _) ->
                println("• ${type.simpleName} [qualifier=${qualifier?.simpleName ?: "none"}]")
            }
        }
        println("============================")
    }

    private fun findQualifier(annotations: List<Annotation>): KClass<*>? =
        annotations.firstOrNull { it.annotationClass.findAnnotation<Qualifier>() != null }?.annotationClass

    private fun <T : Any> getInstance(
        type: KClass<T>,
        qualifier: KClass<*>? = null,
    ): T {
        // 1. qualifier 있는 경우, 등록된 바인딩 중에서 qualifier 일치하는 팩토리 사용
        val qualifierMap = bindings[type]
        val instanceProvider =
            qualifierMap?.get(qualifier)
                ?: qualifierMap?.get(null)

        if (instanceProvider != null) {
            return instanceProvider() as T
        }

        // qualifier가 있는 경우, 그 qualifier에 해당하는 구현체를 자동 선택
        val autoMatch = getQualifiedBinding(type, qualifier)
        if (autoMatch != null) {
            return autoMatch() as T
        }

        // 등록된 게 없으면 autoCreate 시도
        return autoCreate(type)
    }

    private fun <T : Any> autoCreate(type: KClass<T>): T {
        // 1. @Inject 붙은 생성자 우선 선택, 없으면 첫 번째 생성자 사용
        val constructor =
            type.constructors.find { it.findAnnotation<Inject>() != null }
                ?: type.constructors.firstOrNull()
                ?: throw IllegalArgumentException("${type.simpleName}: 생성자를 찾을 수 없습니다.")

        // 2. 생성자 인자에 맞는 의존성들을 Map<KParameter, Any?> 형태로 준비
        val args = mutableMapOf<kotlin.reflect.KParameter, Any?>()

        for (param in constructor.parameters) {
            val dependencyClass =
                param.type.classifier as? KClass<*>
                    ?: continue // 타입을 알 수 없는 경우 스킵

            // Qualifier 찾아서 resolve 시도
            val qualifier = findQualifier(param.annotations)

            // optional (기본값 존재) or nullable 이면 생략 가능
            if (!param.isOptional && !param.type.isMarkedNullable) {
                // 주입 가능한 인스턴스를 DIContainer에서 가져옴
                val dependency = getInstance(dependencyClass as KClass<Any>, qualifier)
                args[param] = dependency
            }
            println("🔍 resolving ${dependencyClass.simpleName} with qualifier=$qualifier")
        }

        // 3. callBy()로 생성자 호출
        return constructor.callBy(args)
    }

    private fun <T : Any> getQualifiedBinding(
        type: KClass<T>,
        qualifier: KClass<*>?,
    ): (() -> Any)? {
        if (qualifier == null) return null
        val entry =
            bindings.entries.find { (registeredType, qualifierMap) ->
                // 직접 타입이 일치하거나 인터페이스를 구현했는지 확인
                (registeredType == type || registeredType.java.interfaces.any { it.kotlin == type }) &&
                    qualifierMap.containsKey(qualifier)
            }
        return entry?.value?.get(qualifier)
    }
}
