package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class AppContainerStore(
    vararg factories: DependencyFactory<*>,
) {
    private val cache: MutableMap<KClass<*>, Any> = mutableMapOf()
    private val factory = factories.associateBy { it.type }

    operator fun get(clazz: KClass<*>): Any? = cache[clazz]

    fun instantiate(
        clazz: KClass<*>,
        saveToCache: Boolean = true,
    ): Any? {
        val inProgress = mutableSetOf<KClass<*>>()
        return instantiate(clazz, inProgress, saveToCache)
    }

    private fun instantiate(
        clazz: KClass<*>,
        inProgress: MutableSet<KClass<*>>,
        saveToCache: Boolean = true,
    ): Any? {
        if (cache.containsKey(clazz)) return this[clazz]

        if (inProgress.contains(clazz)) {
            throw IllegalArgumentException(
                "$ERR_CIRCULAR_DEPENDENCY_DETECTED : ${clazz.simpleName}",
            )
        }

        inProgress.add(clazz)
        val instance =
            factory[clazz]?.invoke() ?: clazz.primaryConstructor?.let {
                reflect(it, inProgress)
            } ?: throw IllegalArgumentException("$ERR_CONSTRUCTOR_NOT_FOUND : ${clazz.simpleName}")

        if (saveToCache) cache[clazz] = instance
        inProgress.remove(clazz)
        return instance
    }

    private fun reflect(
        constructor: KFunction<Any>,
        inProgress: MutableSet<KClass<*>>,
    ): Any {
        val arguments =
            constructor.parameters
                .filter { !it.isOptional }
                .associateWith { parameter ->
                    val instance = instantiate(parameter.type.jvmErasure, inProgress)
                    instance
                }
        return constructor.callBy(arguments)
    }

    companion object {
        private const val ERR_CIRCULAR_DEPENDENCY_DETECTED = "순환 참조가 발견되었습니다"
        private const val ERR_CONSTRUCTOR_NOT_FOUND =
            "주 생성자를 찾을 수 없습니다. 인터페이스, 추상 클래스의 경우 AppContainer에 구현체를 등록해주세요"
    }
}
