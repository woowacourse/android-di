package com.example.di_v2

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.di_v2.annotation.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@Suppress("UNCHECKED_CAST")
class DIContainer {
    private val bindings = mutableMapOf<KClass<*>, MutableMap<KClass<*>?, () -> Any>>()

    // 스코프별 인스턴스 캐시
    private val appCache = mutableMapOf<KClass<*>, Any>()
    private val activityCache = mutableMapOf<Any, MutableMap<KClass<*>, Any>>() // Activity별
    private val viewModelCache = mutableMapOf<Any, MutableMap<KClass<*>, Any>>() // ViewModel별

    // Lifecycle 추적 중인 Owner 기록
    private val observedOwners = mutableSetOf<Any>()

    fun <T : Any> resolve(
        type: KClass<T>,
        qualifier: KClass<*>? = null,
        owner: Any? = null,
    ): T {
        val scope = inferScopeFromAnnotations(type)

        // LifecycleOwner 자동 추적 등록
        if (owner is LifecycleOwner && !observedOwners.contains(owner)) {
            owner.lifecycle.addObserver(ScopedLifecycleObserver(this, owner))
            observedOwners.add(owner)
        }

        // ViewModel 자동 추적 등록
        if (owner is ViewModel && !observedOwners.contains(owner)) {
            if (owner is ScopedViewModel) {
                owner.diContainer = this
            }
            observedOwners.add(owner)
        }

        return when (scope) {
            Scope.APP -> resolveScopedInstance(type, appCache, qualifier)
            Scope.ACTIVITY -> resolveOwnerScopedInstance(type, owner, activityCache, qualifier)
            Scope.VIEWMODEL -> resolveOwnerScopedInstance(type, owner, viewModelCache, qualifier)
            Scope.UNSCOPED -> autoCreate(type, owner)
        }
    }

    fun <T : Any> register(
        type: KClass<T>,
        qualifier: KClass<*>? = null,
        factory: () -> T,
    ) {
        val map = bindings.getOrPut(type) { mutableMapOf() }
        map[qualifier] = factory
    }

    fun inject(instance: Any) {
        val kClass = instance::class
        kClass.memberProperties.forEach { injectedProperty ->
            if (injectedProperty.annotations.any { it.annotationClass == Inject::class } &&
                injectedProperty is KMutableProperty1
            ) {
                val qualifier = findQualifier(injectedProperty.annotations)
                val dependencyType =
                    injectedProperty.returnType.classifier as? KClass<*>
                        ?: throw IllegalStateException("Cannot resolve type for property: ${injectedProperty.name}")
                val dependency = getInstance(dependencyType as KClass<Any>, qualifier)
                injectedProperty.isAccessible = true
                injectedProperty.setter.call(instance, dependency)
            }
        }
    }

    // 기본 인스턴스 생성 로직
    private fun <T : Any> getInstance(
        type: KClass<T>,
        qualifier: KClass<*>? = null,
    ): T {
        val qualifierMap = bindings[type]
        val instanceProvider =
            if (qualifier != null) qualifierMap?.get(qualifier) else qualifierMap?.get(null)

        if (instanceProvider != null) {
            return instanceProvider() as T
        }

        return autoCreate(type)
    }

    // 생성자 기반 자동 주입 (Context 자동 매핑 포함)
    private fun <T : Any> autoCreate(
        type: KClass<T>,
        owner: Any? = null,
    ): T {
        val constructor =
            type.constructors.find { it.findAnnotation<Inject>() != null }
                ?: type.constructors.firstOrNull()
                ?: throw IllegalArgumentException("${type.simpleName}: 생성자를 찾을 수 없습니다.")

        val args = mutableMapOf<kotlin.reflect.KParameter, Any?>()

        for (param in constructor.parameters) {
            val dependencyClass = param.type.classifier as? KClass<*> ?: continue
            val qualifier = findQualifier(param.annotations)

            val dependency =
                when {
                    // Context 자동 주입
                    dependencyClass == android.content.Context::class -> {
                        if (owner is android.content.Context) {
                            owner
                        } else {
                            throw IllegalArgumentException("Context를 주입할 수 있는 owner가 없습니다.")
                        }
                    }

                    else -> getInstance(dependencyClass as KClass<Any>, qualifier)
                }

            args[param] = dependency
        }

        return constructor.callBy(args)
    }

    // 스코프 추론
    private fun inferScopeFromAnnotations(type: KClass<*>): Scope =
        when {
            type.findAnnotation<AppScoped>() != null -> Scope.APP
            type.findAnnotation<ActivityScoped>() != null -> Scope.ACTIVITY
            type.findAnnotation<ViewModelScoped>() != null -> Scope.VIEWMODEL
            else -> Scope.UNSCOPED
        }

    // Qualifier 찾기
    private fun findQualifier(annotations: List<Annotation>): KClass<*>? =
        annotations.firstOrNull { it.annotationClass.findAnnotation<Qualifier>() != null }?.annotationClass

    // Application 스코프
    private fun <T : Any> resolveScopedInstance(
        type: KClass<T>,
        cache: MutableMap<KClass<*>, Any>,
        qualifier: KClass<*>?,
    ): T = cache.getOrPut(type) { getInstance(type, qualifier) } as T

    // Activity / ViewModel 스코프
    private fun <T : Any> resolveOwnerScopedInstance(
        type: KClass<T>,
        owner: Any?,
        cacheMap: MutableMap<Any, MutableMap<KClass<*>, Any>>,
        qualifier: KClass<*>?,
    ): T {
        if (owner == null) return getInstance(type, qualifier)
        val ownerCache = cacheMap.getOrPut(owner) { mutableMapOf() }
        return ownerCache.getOrPut(type) { autoCreate(type, owner) } as T
    }

    // 스코프 해제 (onDestroy, onCleared에서 자동 호출)
    fun clearScope(owner: Any) {
        activityCache.remove(owner)
        viewModelCache.remove(owner)
        observedOwners.remove(owner)
    }
}
