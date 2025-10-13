package com.example.di

import com.example.di.annotation.Inject
import com.example.di.annotation.Provides
import com.example.di.annotation.Singleton
import com.example.di.model.BindingKey
import com.example.di.model.FactoryProvider
import com.example.di.model.InstanceProvider
import com.example.di.model.Provider
import com.example.di.util.findSingleQualifierOrNull
import com.example.di.util.toBindingKey
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
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
        bindings[key] = InstanceProvider(instance)
    }

    /** object 모듈을 스캔하여 @Provides 함수/프로퍼티 등록 */
    fun <T : Any> provideModule(module: KClass<T>) {
        val instance = requireModuleObject(module)
        registerProvideFunctions(module, instance)
        registerPublicProperties(module, instance)
    }

    /** 타입(+선택: Qualifier)으로 인스턴스 획득 */
    fun <T : Any> resolve(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        overrides: Map<BindingKey, Any> = emptyMap(),
    ): T {
        findOverride(overrides, type, qualifier)?.let { return it }
        findBoundProvider(type, qualifier)?.let { return it.get(overrides) as T }

        val created = createByConstructor(type, overrides)
        cacheIfSingleton(type, qualifier, created)
        return created
    }

    /* ============================
     * Module registration
     * ============================ */

    private fun <T : Any> requireModuleObject(module: KClass<T>): T {
        val instance = module.objectInstance
        requireNotNull(instance) { ERROR_MODULE_MUST_BE_OBJECT.format(module.qualifiedName) }
        return instance
    }

    /** @Provides 함수들을 FactoryProvider로 등록 */
    private fun <T : Any> registerProvideFunctions(
        module: KClass<T>,
        instance: T,
    ) {
        module
            .declaredMemberFunctions
            .filter(::isProvidesFunction)
            .forEach { fn ->
                val key = buildProvidesKey(fn)
                val provider = buildFactoryProvider(fn as KFunction<Any>, instance)
                bindings[key] = provider
            }
    }

    /** 모듈의 public val/var 아닌 프로퍼티(= 읽기 전용 프로퍼티) 즉시 등록 */
    private fun <T : Any> registerPublicProperties(
        module: KClass<T>,
        instance: T,
    ) {
        module.memberProperties.forEach { prop ->
            // var 제외(설정가능 프로퍼티는 외부 주입 대상이 될 수 있으므로 자동 등록 지양)
            if (prop is KMutableProperty1<*, *>) return@forEach

            prop.isAccessible = true
            val value = prop.get(instance) ?: return@forEach

            val returnType = prop.returnType.jvmErasure as KClass<Any>
            val qualifier = findSingleQualifierOrNull(prop.annotations)
            val key = BindingKey(returnType, qualifier)

            if (bindings[key] != null) return@forEach
            bindings[key] = InstanceProvider(value)
        }
    }

    /* ============================
     * Resolve helpers
     * ============================ */

    private fun <T : Any> findOverride(
        overrides: Map<BindingKey, Any>,
        type: KClass<T>,
        qualifier: KClass<out Annotation>?,
    ): T? {
        val o = overrides[BindingKey(type, qualifier)] ?: return null
        @Suppress("UNCHECKED_CAST")
        return o as T
    }

    private fun <T : Any> findBoundProvider(
        type: KClass<T>,
        qualifier: KClass<out Annotation>?,
    ): Provider<out Any>? = bindings[BindingKey(type, qualifier)]

    private fun <T : Any> cacheIfSingleton(
        type: KClass<T>,
        qualifier: KClass<out Annotation>?,
        instance: T,
    ) {
        val isSingleton = type.findAnnotation<Singleton>() != null
        if (!isSingleton) return
        bindings[BindingKey(type, qualifier)] = InstanceProvider(instance)
    }

    /* ============================
     * Construction & Injection
     * ============================ */

    /** 생성자 생성 + @Inject 프로퍼티 주입 */
    private fun <T : Any> createByConstructor(
        type: KClass<T>,
        overrides: Map<BindingKey, Any>,
    ): T {
        val constructor =
            requireNotNull(type.primaryConstructor) {
                ERROR_NO_PUBLIC_CTOR.format(type.simpleName ?: "Anonymous")
            }
        val args = buildConstructorArgs(constructor, overrides)
        val instance = constructor.callBy(args)
        injectProperties(instance, overrides)
        return instance
    }

    /** 생성자 인자 맵 생성(한 파라미터 = 한 책임) */
    private fun buildConstructorArgs(
        constructor: KFunction<*>,
        overrides: Map<BindingKey, Any>,
    ): Map<KParameter, Any?> =
        constructor.parameters
            .mapNotNull { param ->
                val instance =
                    resolveConstructorParameter(param, overrides) ?: return@mapNotNull null
                param to instance
            }.toMap()

    /** 단일 생성자 파라미터 해석 */
    private fun resolveConstructorParameter(
        param: KParameter,
        overrides: Map<BindingKey, Any>,
    ): Any? {
        val hasInject = param.findAnnotation<Inject>() != null
        if (!hasInject) {
            // @Inject 아님 → 기본값 있으면 생략(맵에 넣지 않음)
            if (param.isOptional) return null
            // 기본값도 없으면 오류
            throw IllegalArgumentException(ERROR_PARAM_NOT_AUTOWIRE.format(param))
        }

        val key = param.toBindingKey()
        overrides[key]?.let { return it }

        val kClass =
            requireNotNull(param.type.classifier as? KClass<out Any>) {
                ERROR_UNKNOWN_TYPE.format(param)
            }
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
            .filter { prop -> prop.hasAnnotation<Inject>() }
            .forEach { prop ->
                val qualifier = findSingleQualifierOrNull(prop.annotations)
                val kClass = prop.returnType.jvmErasure as KClass<Any>

                // 이미 값이 있으면 스킵
                prop.isAccessible = true
                val current = runCatching { prop.get(target) }.getOrNull()
                if (current != null) return@forEach

                val value = resolve(kClass, qualifier, overrides)
                prop.set(target, value)
            }
    }

    /* ============================
     * @Provides helpers
     * ============================ */

    private fun isProvidesFunction(fn: KFunction<*>): Boolean = fn.findAnnotation<Provides>() != null

    private fun buildProvidesKey(fn: KFunction<*>): BindingKey {
        val returnType = fn.returnType.jvmErasure
        val qualifier = findSingleQualifierOrNull(fn.annotations)
        return BindingKey(returnType, qualifier)
    }

    private fun <T : Any> buildFactoryProvider(
        fn: KFunction<T>,
        moduleInstance: Any,
    ): FactoryProvider<Any> {
        fn.isAccessible = true

        val isSingleton = fn.findAnnotation<Singleton>() != null

        val factory: (Map<BindingKey, Any>) -> Any = { overrides ->
            val args = buildProvidesArgs(fn, moduleInstance, overrides)
            fn.callBy(args)
        }
        return FactoryProvider(
            factory = factory,
            isSingleton = isSingleton,
        )
    }

    /** @Provides 함수 인자 맵 생성(모듈 인스턴스 + 값 파라미터) */
    private fun buildProvidesArgs(
        fn: KFunction<*>,
        moduleInstance: Any,
        overrides: Map<BindingKey, Any>,
    ): Map<KParameter, Any?> =
        fn.parameters.associateWith { param ->
            if (param.kind == KParameter.Kind.INSTANCE) return@associateWith moduleInstance
            resolveProvidesParameter(param, overrides)
        }

    /** 단일 @Provides 파라미터 해석 */
    private fun resolveProvidesParameter(
        param: KParameter,
        overrides: Map<BindingKey, Any>,
    ): Any {
        // @Provides 파라미터도 @Inject로 자동 주입 대상을 명시적으로 제한
        require(param.findAnnotation<Inject>() != null) {
            ERROR_PARAM_NOT_AUTOWIRE.format(param)
        }

        val key = param.toBindingKey()
        overrides[key]?.let { return it }

        val kClass =
            requireNotNull(param.type.classifier as? KClass<out Any>) {
                ERROR_UNKNOWN_TYPE.format(param)
            }
        return resolve(kClass, key.qualifier, overrides)
    }

    companion object {
        const val ERROR_MODULE_MUST_BE_OBJECT = "모듈은 object여야 합니다: %s"
        const val ERROR_NO_PUBLIC_CTOR = "%s에 public 생성자가 없습니다."
        const val ERROR_PARAM_NOT_AUTOWIRE =
            "자동 주입 대상이 아닌 파라미터입니다. @Inject를 붙이거나 모듈 @Provides를 사용하세요: %s"
        const val ERROR_UNKNOWN_TYPE = "타입 정보를 알 수 없습니다: %s"
    }
}

/** reified 헬퍼 */
inline fun <reified T : Any> AppContainer.provide(
    instance: T,
    qualifier: KClass<out Annotation>? = null,
) = provide(T::class, instance, qualifier)
