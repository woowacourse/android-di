package com.example.di_v2

import com.example.di_v2.annotation.ActivityScoped
import com.example.di_v2.annotation.AppScoped
import com.example.di_v2.annotation.Inject
import com.example.di_v2.annotation.Qualifier
import com.example.di_v2.annotation.Scope
import com.example.di_v2.annotation.ViewModelScoped
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@Suppress("UNCHECKED_CAST")
class DIContainer {
    // 각 타입별로 등록된 팩토리 함수를 보관
    private val bindings = mutableMapOf<KClass<*>, MutableMap<KClass<*>?, () -> Any>>()

    // 스코프별 인스턴스 캐시 (생명주기에 맞게 객체를 재사용하기 위해 사용)
    private val appCache = mutableMapOf<KClass<*>, Any>()
    private val activityCache = mutableMapOf<Any, MutableMap<KClass<*>, Any>>() // Activity 별 캐시
    private val viewModelCache = mutableMapOf<Any, MutableMap<KClass<*>, Any>>() // ViewModel 별 캐시

    // 의존성을 조회하거나 생성하는 메서드
    fun <T : Any> resolve(
        type: KClass<T>,
        qualifier: KClass<*>? = null,
        owner: Any? = null,
    ): T {
        // 클래스에 붙은 스코프 어노테이션을 기반으로 어떤 범위에서 관리할지 결정
        val scope = inferScopeFromAnnotations(type)
        return when (scope) {
            Scope.APP -> resolveScopedInstance(type, appCache, qualifier)
            Scope.ACTIVITY -> resolveOwnerScopedInstance(type, owner, activityCache, qualifier)
            Scope.VIEWMODEL -> resolveOwnerScopedInstance(type, owner, viewModelCache, qualifier)
            Scope.UNSCOPED -> getInstance(type, qualifier)
        }
    }

    // 타입과 생성 방식을 등록하는 메서드
    fun <T : Any> register(
        type: KClass<T>,
        qualifier: KClass<*>? = null,
        factory: () -> T,
    ) {
        val map = bindings.getOrPut(type) { mutableMapOf() }
        map[qualifier] = factory
    }

    // @Inject 프로퍼티가 붙은 필드에 의존성을 주입
    fun inject(instance: Any) {
        val kClass = instance::class
        kClass.memberProperties.forEach { injectedProperty ->
            // @Inject가 붙은 수정 가능한 프로퍼티만 주입 대상
            if (injectedProperty.annotations.any { it.annotationClass == Inject::class } &&
                injectedProperty is KMutableProperty1
            ) {
                val qualifier = findQualifier(injectedProperty.annotations)
                // 주입 대상 타입 추론
                val dependencyType =
                    injectedProperty.returnType.classifier as? KClass<*>
                        ?: throw IllegalStateException("${injectedProperty.name} 타입을 추론할 수 없습니다")
                // 의존성 생성 또는 조회
                val dependency = getInstance(dependencyType as KClass<Any>, qualifier)

                // 접근 가능하도록 설정한 뒤, setter를 호출해 값 주입
                injectedProperty.isAccessible = true
                injectedProperty.setter.call(instance, dependency)
            }
        }
    }

    // 스코프 해제 (예: onDestroy, onCleared 시)
    fun clearScope(owner: Any) {
        activityCache.remove(owner)
        viewModelCache.remove(owner)
    }

    // 등록된 바인딩을 통해 인스턴스를 가져오거나, 등록되지 않았다면 자동 생성(autoCreate) 시도
    private fun <T : Any> getInstance(
        type: KClass<T>,
        qualifier: KClass<*>? = null,
    ): T {
        val qualifierMap = bindings[type]

        // qualifier 유무에 따라 올바른 팩토리 선택
        val instanceProvider =
            if (qualifier != null) {
                qualifierMap?.get(qualifier)
            } else {
                qualifierMap?.get(null)
            }

        if (instanceProvider != null) {
            return instanceProvider() as T
        }

        return autoCreate(type)
    }

    private fun <T : Any> autoCreate(type: KClass<T>): T {
        // 1. @Inject가 붙은 생성자가 있으면 우선 사용, 없으면 기본 생성자 사용
        val constructor =
            type.constructors.find { it.findAnnotation<Inject>() != null }
                ?: type.constructors.firstOrNull()
                ?: throw IllegalArgumentException("${type.simpleName}: 생성자를 찾을 수 없습니다.")

        // 2. 생성자 파라미터에 맞춰 의존성 주입
        val args = mutableMapOf<kotlin.reflect.KParameter, Any?>()
        for (param in constructor.parameters) {
            val dependencyClass =
                param.type.classifier as? KClass<*> ?: continue
            val qualifier = findQualifier(param.annotations)
            if (!param.isOptional && !param.type.isMarkedNullable) {
                val dependency = getInstance(dependencyClass as KClass<Any>, qualifier)
                args[param] = dependency
            }
        }

        // 3. 준비된 인자를 기반으로 생성자 호출
        return constructor.callBy(args)
    }

    // 클래스에 붙은 스코프 어노테이션을 확인해 해당 객체의 생명주기 스코프를 추론
    private fun inferScopeFromAnnotations(type: KClass<*>): Scope =
        when {
            type.findAnnotation<AppScoped>() != null -> Scope.APP
            type.findAnnotation<ActivityScoped>() != null -> Scope.ACTIVITY
            type.findAnnotation<ViewModelScoped>() != null -> Scope.VIEWMODEL
            else -> Scope.UNSCOPED
        }

    // @Qualifier 어노테이션이 붙은 식별자(예: @Database, @InMemory)를 탐색
    private fun findQualifier(annotations: List<Annotation>): KClass<*>? =
        annotations.firstOrNull { it.annotationClass.findAnnotation<Qualifier>() != null }?.annotationClass

    // Application 범위 캐시 관리
    private fun <T : Any> resolveScopedInstance(
        type: KClass<T>,
        cache: MutableMap<KClass<*>, Any>,
        qualifier: KClass<*>?,
    ): T = cache.getOrPut(type) { getInstance(type, qualifier) } as T

    // Activity/ViewModel Owner 범위 캐시 관리
    private fun <T : Any> resolveOwnerScopedInstance(
        type: KClass<T>,
        owner: Any?,
        cacheMap: MutableMap<Any, MutableMap<KClass<*>, Any>>,
        qualifier: KClass<*>?,
    ): T {
        if (owner == null) return getInstance(type, qualifier)
        val ownerCache = cacheMap.getOrPut(owner) { mutableMapOf() }
        return ownerCache.getOrPut(type) { getInstance(type, qualifier) } as T
    }
}
