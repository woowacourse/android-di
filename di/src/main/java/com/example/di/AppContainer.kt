package com.example.di

import com.example.di.model.BindingKey
import com.example.di.model.FactoryProvider
import com.example.di.model.Provider
import com.example.di.model.SingletonProvider
import com.example.di.util.findSingleQualifierOrNull
import com.example.di.util.isProvidesFunction
import com.example.di.util.isSingletonFunction
import com.example.di.util.requireInject
import com.example.di.util.requireModuleObject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

@Suppress("UNCHECKED_CAST")
class AppContainer {
    private val bindings = mutableMapOf<BindingKey, Provider<out Any>>()

    /** 인스턴스를 직접 바인딩 */
    fun <T : Any> provide(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        val key = BindingKey(type, qualifier)
        bindings[key] = SingletonProvider { instance }
    }

    /** object 모듈을 스캔하여 @Provides 함수들을 FactoryProvider로 등록 */
    fun <T : Any> provideModule(module: KClass<T>) {
        module
            .declaredMemberFunctions
            .filter { fn -> fn.isProvidesFunction() }
            .forEach { fn ->
                val key = BindingKey.from(fn)
                val provider = buildProvider(fn as KFunction<Any>, requireModuleObject(module))
                bindings[key] = provider
            }
    }

    /** 타입(+선택: Qualifier)으로 인스턴스 획득 */
    fun <T : Any> resolve(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        overrides: Map<BindingKey, Any> = emptyMap(),
    ): T {
        findOverride(overrides, type, qualifier)?.let { return it }
        findBoundProvider(type, qualifier)?.let { return it.get(overrides) as T }

        return createByConstructor(type, overrides)
    }

    /** overrides에서 타입(+선택: Qualifier)으로 인스턴스 찾기 */
    private fun <T : Any> findOverride(
        overrides: Map<BindingKey, Any>,
        type: KClass<T>,
        qualifier: KClass<out Annotation>?,
    ): T? = overrides[BindingKey(type, qualifier)] as? T

    /** bindings에서 타입(+선택: Qualifier)으로 Provider 찾기 */
    private fun <T : Any> findBoundProvider(
        type: KClass<T>,
        qualifier: KClass<out Annotation>?,
    ): Provider<out Any>? = bindings[BindingKey(type, qualifier)]

    /** 생성자 생성 + @Inject 프로퍼티 주입 */
    private fun <T : Any> createByConstructor(
        type: KClass<T>,
        overrides: Map<BindingKey, Any>,
    ): T {
        val constructor =
            requireNotNull(type.primaryConstructor) {
                ERROR_NO_PUBLIC_CTOR.format(type.simpleName ?: "Anonymous")
            }
        val instance = buildConstructorArgs(constructor, overrides).let(constructor::callBy)
        injectProperties(instance, overrides)
        return instance
    }

    /** 생성자 인자 맵 생성(한 파라미터 = 한 책임) */
    private fun buildConstructorArgs(
        constructor: KFunction<*>,
        overrides: Map<BindingKey, Any>,
    ): Map<KParameter, Any> =
        constructor.parameters
            .mapNotNull { param ->
                resolveConstructorParameter(param, overrides)?.let { instance ->
                    param to instance
                } ?: return@mapNotNull null
            }.toMap()

    /**
     * 단일 생성자 파라미터 해석 규칙
     * - @Inject 없음 + optional: 생략(null) → callBy에서 기본값 사용
     * - @Inject 없음 + optional 아님: 오류
     * - overrides에 존재: 최우선 반환
     * - 그 외: 컨테이너에서 (타입, 한정자)로 resolve
     */
    private fun resolveConstructorParameter(
        param: KParameter,
        overrides: Map<BindingKey, Any>,
    ): Any? {
        if (param.requireInject().not()) {
            require(param.isOptional) { ERROR_PARAM_NOT_AUTOWIRE.format(param) }
            return null
        }

        val key = BindingKey.from(param)
        overrides[key]?.let { return it }

        val kClass = param.type.classifier as KClass<out Any>
        return resolve(kClass, key.qualifier, overrides)
    }

    /** @Inject 프로퍼티 주입(이미 값이 있으면 스킵) */
    private fun injectProperties(
        target: Any,
        overrides: Map<BindingKey, Any> = emptyMap(),
    ) {
        target::class
            .memberProperties
            .filterIsInstance<KMutableProperty1<Any, Any?>>()
            .filter { prop -> prop.requireInject() }
            .forEach { prop ->
                prop.isAccessible = true
                val qualifier = findSingleQualifierOrNull(prop.annotations)
                val kClass = prop.returnType.jvmErasure as KClass<Any>

                val current = runCatching { prop.get(target) }.getOrNull()
                if (current != null) return@forEach

                val value = resolve(kClass, qualifier, overrides)
                prop.set(target, value)
            }
    }

    /** @Provides Annotation이 붙은 함수로부터 Provider를 생성한다. */
    private fun <T : Any> buildProvider(
        fn: KFunction<T>,
        moduleInstance: Any,
    ): Provider<Any> {
        fn.isAccessible = true
        val factory: (Map<BindingKey, Any>) -> Any = { overrides ->
            val args = buildProvidesArgs(fn, moduleInstance, overrides)
            fn.callBy(args)
        }

        if (fn.isSingletonFunction()) return SingletonProvider(factory)
        return FactoryProvider(factory)
    }

    /** @Provides 함수 인자 맵 생성(모듈 인스턴스 + 값 파라미터) */
    private fun buildProvidesArgs(
        fn: KFunction<*>,
        moduleInstance: Any,
        overrides: Map<BindingKey, Any>,
    ): Map<KParameter, Any> =
        fn.parameters
            .mapNotNull { param ->
                if (param.kind == KParameter.Kind.INSTANCE) return@mapNotNull param to moduleInstance
                val resolved = resolveProvidesParameter(param, overrides) ?: return@mapNotNull null
                param to resolved
            }.toMap()

    /** 단일 @Provides 파라미터 해석 */
    private fun resolveProvidesParameter(
        param: KParameter,
        overrides: Map<BindingKey, Any>,
    ): Any? {
        if (param.requireInject().not()) return null

        val key = BindingKey.from(param)
        overrides[key]?.let { return it }

        val kClass = param.type.classifier as KClass<out Any>
        return resolve(kClass, key.qualifier, overrides)
    }

    companion object {
        private const val ERROR_NO_PUBLIC_CTOR = "%s에 public 생성자가 없습니다."
        private const val ERROR_PARAM_NOT_AUTOWIRE =
            "자동 주입 대상이 아닌 파라미터입니다. @Inject를 붙이거나 모듈 @Provides를 사용하세요: %s"
    }
}

/** reified 헬퍼 */
inline fun <reified T : Any> AppContainer.provide(
    instance: T,
    qualifier: KClass<out Annotation>? = null,
) = provide(T::class, instance, qualifier)
