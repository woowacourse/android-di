package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class AppContainerStore {
    private val cache: MutableMap<Qualifier, Any> = mutableMapOf()
    private val factory = mutableMapOf<Qualifier, DependencyFactory<*>>()

    operator fun get(clazz: KClass<*>): Any? = cache[Qualifier(clazz)]

    fun instantiate(
        qualifier: Qualifier,
        saveToCache: Boolean = true,
    ): Any {
        val inProgress = mutableSetOf<Qualifier>()
        return instantiate(qualifier, inProgress, saveToCache)
    }

    fun registerFactory(vararg factories: DependencyFactory<*>) {
        factory.putAll(factories.associateBy { it.qualifier })
    }

    private fun instantiate(
        qualifier: Qualifier,
        inProgress: MutableSet<Qualifier>,
        saveToCache: Boolean = true,
    ): Any {
        if (cache.containsKey(qualifier)) {
            return cache[qualifier] ?: throw IllegalArgumentException(
                "$ERR_CONSTRUCTOR_NOT_FOUND : ${qualifier.type.simpleName}",
            )
        }

        if (inProgress.contains(qualifier)) {
            throw IllegalArgumentException(
                "$ERR_CIRCULAR_DEPENDENCY_DETECTED : ${qualifier.type.simpleName}",
            )
        }

        inProgress.add(qualifier)
        val instance =
            factory[qualifier]?.invoke() ?: qualifier.type.primaryConstructor?.let {
                reflect(it, inProgress)
            }
                ?: throw IllegalArgumentException("$ERR_CONSTRUCTOR_NOT_FOUND : ${qualifier.type.simpleName}")

        injectField(instance, inProgress)
        if (saveToCache) cache[qualifier] = instance
        inProgress.remove(qualifier)
        return instance
    }

    private fun reflect(
        constructor: KFunction<Any>,
        inProgress: MutableSet<Qualifier>,
    ): Any {
        val arguments =
            constructor.parameters
                .filter { !it.isOptional }
                .associateWith { parameter ->
                    val childQualifier =
                        Qualifier(
                            parameter.type.jvmErasure,
                            parameter.findAnnotation<Inject>()?.name,
                        )

                    instantiate(
                        childQualifier,
                        inProgress,
                    )
                }
        return constructor.callBy(arguments)
    }

    private fun injectField(
        instance: Any,
        inProgress: MutableSet<Qualifier>,
    ) {
        try {
            instance::class.memberProperties
        } catch (e: Error) {
            // 코틀린 리플렉션이 지원하지 않는 프레임워크 객체는 건너뜁니다
            if (e::class.simpleName == "KotlinReflectionInternalError") return
        }
        instance::class
            .memberProperties
            .filterIsInstance<KMutableProperty1<Any, Any>>()
            .filter { it.findAnnotation<Inject>() != null }
            .forEach { property ->
                val childQualifier =
                    Qualifier(
                        property.returnType.jvmErasure,
                        property.findAnnotation<Inject>()?.name,
                    )
                property.set(
                    instance,
                    instantiate(
                        childQualifier,
                        inProgress,
                    ),
                )
            }
    }

    companion object {
        private const val ERR_FAILED_FIELD_INJECTION = "필드 주입에 실패했습니다"
        private const val ERR_CIRCULAR_DEPENDENCY_DETECTED = "순환 참조가 발견되었습니다"
        private const val ERR_CONSTRUCTOR_NOT_FOUND =
            "등록된 팩토리, 또는 주 생성자를 찾을 수 없습니다"
    }
}
