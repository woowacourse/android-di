package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class AppContainerStore {
    private val cache: MutableMap<Qualifier, Any> = mutableMapOf()
    private val factory = mutableMapOf<Qualifier, DependencyFactory<*>>()

    operator fun get(clazz: KClass<*>): Any? = cache[Qualifier(clazz)]

    fun instantiate(
        qualifier: Qualifier,
        saveToCache: Boolean = true,
    ): Any? {
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
    ): Any? {
        if (cache.containsKey(qualifier)) return cache[qualifier]

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

    companion object {
        private const val ERR_CIRCULAR_DEPENDENCY_DETECTED = "순환 참조가 발견되었습니다"
        private const val ERR_CONSTRUCTOR_NOT_FOUND =
            "등록된 팩토리, 또는 주 생성자를 찾을 수 없습니다"
    }
}
