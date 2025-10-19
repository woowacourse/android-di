package com.example.di

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.di.annotation.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@Suppress("UNCHECKED_CAST")
class DIContainer {
    private val bindings = mutableMapOf<KClass<*>, MutableMap<KClass<*>?, (Any?) -> Any>>()

    // 스코프별 인스턴스 캐시
    private val appCache = mutableMapOf<KClass<*>, Any>()
    private val activityCache = mutableMapOf<Any, MutableMap<KClass<*>, Any>>()
    private val viewModelCache = mutableMapOf<Any, MutableMap<KClass<*>, Any>>()

    // Lifecycle 추적 중인 Owner 기록
    private val observedOwners = mutableSetOf<Any>()

    fun <T : Any> register(
        type: KClass<T>,
        qualifier: KClass<*>? = null,
        factory: () -> T,
    ) {
        val map = bindings.getOrPut(type) { mutableMapOf() }
        map[qualifier] = { _ -> factory() }
    }

    fun <T : Any> registerFactory(
        type: KClass<T>,
        qualifier: KClass<*>? = null,
        factory: (owner: Any?) -> T,
    ) {
        println("[DI][registerFactory] type=${type.simpleName}, qualifier=${qualifier?.simpleName}")

        val map = bindings.getOrPut(type) { mutableMapOf() }
        map[qualifier] = factory
    }

    fun <T : Any> get(
        type: KClass<T>,
        qualifier: KClass<*>? = null,
        owner: Any? = null,
    ): T {
        println("[DI][get] ▶ type=${type.simpleName}, qualifier=${qualifier?.simpleName}, owner=${owner?.javaClass?.simpleName}")

        // 먼저 factory 바인딩이 있으면 바로 resolveFactory() 사용
        if (bindings[type]?.containsKey(qualifier) == true) {
            return resolveFactory(type, qualifier, owner)
        }

        val scope = inferScopeFromAnnotations(type)
        observeIfNeeded(owner)

        return when (scope) {
            Scope.APP -> getAppScoped(type, qualifier)
            Scope.ACTIVITY -> getOwnerScoped(type, owner, activityCache, qualifier)
            Scope.VIEWMODEL -> getOwnerScoped(type, owner, viewModelCache, qualifier)
            Scope.UNSCOPED -> createInstance(type, owner)
        }
    }

    // 생성자 기반 자동 주입
    private fun <T : Any> createInstance(
        type: KClass<T>,
        owner: Any? = null,
    ): T {
        val constructor =
            type.constructors.find { it.findAnnotation<Inject>() != null }
                ?: type.constructors.firstOrNull()
                ?: throw IllegalArgumentException("${type.simpleName}: 생성자를 찾을 수 없습니다.")

        println("[DI][createInstance] ${type.simpleName}을 생성합니다.")
        val args = mutableMapOf<KParameter, Any?>()

        for (param in constructor.parameters) {
            val dependencyClass = param.type.classifier as? KClass<*> ?: continue
            val qualifier = findQualifier(param.annotations)

            val dependency =
                when {
                    dependencyClass == android.content.Context::class -> {
                        owner as? android.content.Context
                            ?: throw IllegalArgumentException("Context를 주입할 수 있는 owner가 없습니다.")
                    }

                    else -> get(dependencyClass as KClass<Any>, qualifier, owner)
                }
            args[param] = dependency
        }

        println("[DI][createInstance] ✅ 사용하는 생성자 파라미터 = ${constructor.parameters.map { it.name }}")

        val instance = constructor.callBy(args)
        injectProperties(instance)
        return instance
    }

    // 프로퍼티 주입 (보조적, 생성자 없는 경우 대비)
    private fun injectProperties(instance: Any) {
        println("[DI][injectProperties] ${instance::class.simpleName}의 프로퍼티에 의존성을 주입")

        instance::class.memberProperties.forEach { property ->
            if (property.annotations.any { it.annotationClass == Inject::class } &&
                property is KMutableProperty1
            ) {
                val qualifier = findQualifier(property.annotations)
                val dependencyType =
                    property.returnType.classifier as? KClass<*>
                        ?: return@forEach
                val dependency = get(dependencyType as KClass<Any>, qualifier)
                property.isAccessible = true
                property.setter.call(instance, dependency)
            }
        }
    }

    // Application 스코프
    private fun <T : Any> getAppScoped(
        type: KClass<T>,
        qualifier: KClass<*>?,
    ): T {
        val cacheKey = qualifier ?: type
        return appCache.getOrPut(cacheKey) {
            println("[DI][Scope]${type.simpleName}의 AppScoped 인스턴스를 새로 생성합니다.")
            resolveFactory(type, qualifier, null)
        } as T
    }

    // Activity / ViewModel 스코프
    private fun <T : Any> getOwnerScoped(
        type: KClass<T>,
        owner: Any?,
        cacheMap: MutableMap<Any, MutableMap<KClass<*>, Any>>,
        qualifier: KClass<*>?,
    ): T {
        if (owner == null) {
            throw IllegalStateException("${type.simpleName}: ${cacheMap::class.simpleName} 스코프에는 owner가 필요합니다.")
        }

        val ownerCache =
            cacheMap.getOrPut(owner) {
                mutableMapOf()
            }

        return ownerCache.getOrPut(type) {
            resolveFactory(type, qualifier, owner)
        } as T
    }

    // factory 실행 로직 (owner-aware 지원)
    private fun <T : Any> resolveFactory(
        type: KClass<T>,
        qualifier: KClass<*>?,
        owner: Any?,
    ): T {
        val factory = bindings[type]?.get(qualifier)

        return if (factory != null) {
            factory(owner) as T
        } else {
            createInstance(type, owner)
        }
    }

    // LifecycleOwner / ViewModel 자동 추적
    private fun observeIfNeeded(owner: Any?) {
        if (owner == null || observedOwners.contains(owner)) return
        when (owner) {
            is LifecycleOwner -> {
                owner.lifecycle.addObserver(ScopedLifecycleObserver(this, owner))
            }
            is ViewModel -> {
                if (owner is ScopedViewModel) owner.diContainer = this
            }
        }
        observedOwners.add(owner)
    }

    private fun findQualifier(annotations: List<Annotation>): KClass<*>? =
        annotations.firstOrNull { it.annotationClass.findAnnotation<Qualifier>() != null }?.annotationClass

    private fun inferScopeFromAnnotations(type: KClass<*>): Scope =
        when {
            type.findAnnotation<AppScoped>() != null -> Scope.APP
            type.findAnnotation<ActivityScoped>() != null -> Scope.ACTIVITY
            type.findAnnotation<ViewModelScoped>() != null -> Scope.VIEWMODEL
            else -> Scope.UNSCOPED
        }

    fun clearScope(owner: Any) {
        println("[DI][Scope] ${owner::class.simpleName}의 스코프를 정리합니다.")
        activityCache.remove(owner)
        viewModelCache.remove(owner)
        observedOwners.remove(owner)
    }
}
