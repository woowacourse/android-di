package woowacourse.bibi.di.core

import kotlin.concurrent.getOrSet
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor

internal class Container(
    private val parent: Container?,
    private val scope: KClass<out Annotation>,
    private val providers: Map<Key, Binding>,
) : AppContainer {
    private val cache = mutableMapOf<Key, Any>()
    private val resolving = ThreadLocal<MutableSet<KType>>()

    override fun resolve(
        type: KType,
        qualifier: KClass<out Annotation>?,
    ): Any {
        val kClass =
            type.classifier as? KClass<*>
                ?: error("지원하지 않는 타입: $type")

        val key = Key(kClass, qualifier)
        val binding = providers[key]

        if (binding != null) {
            val owner = findOwner(binding.scope)
            return owner.cache.computeIfAbsent(key) { binding.provider() }
        }

        if (kClass.isAbstract || kClass.isSealed || kClass.java.isInterface) {
            error("바인딩 누락: $kClass (qualifier=${qualifier?.simpleName})")
        }

        val seen = resolving.getOrSet { mutableSetOf() }
        return seen.withElement(type) { createByConstructorInjection(kClass) }
    }

    override fun child(scope: KClass<out Annotation>): AppContainer = Container(parent = this, scope = scope, providers = providers)

    override fun clear() {
        cache.clear()
    }

    private fun findOwner(targetScope: KClass<out Annotation>): Container =
        when (targetScope) {
            AppScope::class -> root()
            ActivityScope::class -> nearest(ActivityScope::class) ?: root()
            ViewModelScope::class -> nearest(ViewModelScope::class) ?: this
            else -> this
        }

    private fun root(): Container = parent?.root() ?: this

    private fun nearest(target: KClass<out Annotation>): Container? = if (this.scope == target) this else parent?.nearest(target)

    private fun createByConstructorInjection(target: KClass<*>): Any {
        val constructor =
            target.primaryConstructor
                ?: target.constructors.maxByOrNull { it.parameters.size }
                ?: error("생성자 없음: ${target.qualifiedName}")

        val args = constructor.parameters.map { resolve(it.type, null) }.toTypedArray()
        return constructor.call(*args)
    }
}
