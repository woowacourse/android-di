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
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class AppContainer {
    private val bindings = mutableMapOf<BindingKey, Provider<out Any>>()

    /** 인스턴스를 직접 주입 (테스트/수동 바인딩) */
    fun <T : Any> provide(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        val key = BindingKey(type, qualifier)
        bindings[key] = InstanceProvider(instance)
    }

    /** 모듈 object 스캔: @Provides 함수 자동 등록 */
    fun <T : Any> provideModule(module: KClass<T>) {
        val instance = module.objectInstance
        requireNotNull(instance) { "모듈은 object여야 합니다: ${module.qualifiedName}" }

        // 제공 함수 스캔
        module.declaredMemberFunctions
            .filter { it.findAnnotation<Provides>() != null }
            .forEach { fn ->
                val returnType = fn.returnType.jvmErasure
                val qualifier = findSingleQualifierOrNull(fn.annotations)
                val isSingleton = fn.findAnnotation<Singleton>() != null

                val key = BindingKey(returnType, qualifier)

                fn.isAccessible = true

                val provider =
                    FactoryProvider(
                        type = returnType as KClass<Any>,
                        factory = { overrides ->
                            val args =
                                fn.parameters
                                    .associateWith { param ->
                                        // param.kind == INSTANCE -> module object
                                        if (param.kind == KParameter.Kind.INSTANCE) {
                                            instance
                                        } else {
                                            resolveParameter(param, overrides)
                                        }
                                    }
                            fn.callBy(args)!!
                        },
                        isSingleton = isSingleton,
                    )

                bindings[key] = provider
            }

        // 모듈 내 public val/var 프로퍼티도 즉시 등록 (선택)
        module.memberProperties.forEach { prop ->
            if (prop !is KMutableProperty1) {
                prop.isAccessible = true
                val value = prop.get(instance) ?: return@forEach
                val returnType = prop.returnType.jvmErasure as KClass<Any>
                val qualifier = findSingleQualifierOrNull(prop.annotations)
                val key = BindingKey(returnType, qualifier)
                // 함수 바인딩이 이미 있으면 유지
                if (bindings[key] == null) bindings[key] = InstanceProvider(value)
            }
        }
    }

    /** 타입(옵션: Qualifier)로 resolve */
    fun <T : Any> resolve(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        overrides: Map<BindingKey, Any> = emptyMap(),
    ): T {
        // 1) overrides 우선
        val o = overrides[BindingKey(type, qualifier)]
        if (o != null) return o as T

        // 2) 기존 provider
        val provider = bindings[BindingKey(type, qualifier)]
        if (provider != null) return provider.get(overrides) as T

        // 3) 기본 생성자 경로
        return createByConstructor(type, overrides).also { created ->
            // @Singleton 클래스면 캐시
            val isSingleton = type.findAnnotation<Singleton>() != null
            if (isSingleton) {
                bindings[BindingKey(type, qualifier)] = InstanceProvider(created)
            }
        }
    }

    /** T를 리플렉션으로 생성 + @Inject 프로퍼티 주입 */
    private fun <T : Any> createByConstructor(
        type: KClass<T>,
        overrides: Map<BindingKey, Any>,
    ): T {
        val ctor =
            requireNotNull(type.primaryConstructor) {
                "${type.simpleName}에 public 생성자가 없습니다."
            }

        val args =
            ctor.parameters.associateWith { param ->
                resolveParameter(param, overrides)
            }

        val instance = ctor.callBy(args)
        injectProperties(instance, overrides)
        return instance
    }

    /** 파라미터 하나 resolve */
    private fun resolveParameter(
        param: KParameter,
        overrides: Map<BindingKey, Any>,
    ): Any {
        // @Inject가 없는 파라미터면 자동 주입하지 않음
        val needInject = param.findAnnotation<Inject>() != null
        require(needInject) {
            "자동 주입 대상이 아닌 파라미터입니다. @Inject를 붙이거나 모듈 @Provides를 사용하세요: $param"
        }

        val key = param.toBindingKey()
        val override = overrides[key]
        if (override != null) return override

        val classifier =
            requireNotNull(param.type.classifier as? KClass<out Any>) {
                "타입 정보를 알 수 없습니다: $param"
            }
        return resolve(classifier, key.qualifier, overrides)
    }

    /** @Inject 프로퍼티 주입 (var 또는 lateinit var 가능) */
    private fun injectProperties(
        target: Any,
        overrides: Map<BindingKey, Any> = emptyMap(),
    ) {
        target::class
            .memberProperties
            .filterIsInstance<KMutableProperty1<Any, Any?>>()
            .forEach { prop ->
                val inject = prop.findAnnotation<Inject>() != null
                if (!inject) return@forEach

                val qualifier = findSingleQualifierOrNull(prop.annotations)
                val kClass = prop.returnType.jvmErasure as KClass<Any>

                val value = resolve(kClass, qualifier, overrides)
                prop.isAccessible = true

                // 이미 값이 있으면 스킵 (요청사항 반영)
                val current = kotlin.runCatching { prop.get(target) }.getOrNull()
                if (current != null) return@forEach

                prop.set(target, value)
            }
    }
}

/** reified 헬퍼 */
inline fun <reified T : Any> AppContainer.provide(
    instance: T,
    qualifier: KClass<out Annotation>? = null,
) = provide(T::class, instance, qualifier)

inline fun <reified T : Any> AppContainer.resolve(
    qualifier: KClass<out Annotation>? = null,
    overrides: Map<BindingKey, Any> = emptyMap(),
): T = resolve(T::class, qualifier, overrides)
