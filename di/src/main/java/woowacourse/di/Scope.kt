package woowacourse.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.cast
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

class Scope internal constructor(
    private var factories: Map<DependencyKey, (Scope) -> Any>,
    private var parent: Scope? = null,
    private var onClose: (() -> Unit)? = null,
) : AutoCloseable {
    private val instances: MutableMap<DependencyKey, Any> = mutableMapOf()
    private var isClosed: Boolean = false

    internal val instanceCount: Int
        get() = instances.size

    override fun close() {
        isClosed = true
        instances.clear()
        factories = emptyMap()
        parent = null
        val callback = onClose
        onClose = null
        callback?.invoke()
    }

    fun <T : Any> resolve(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        check(!isClosed) { "닫힌 스코프에서는 의존성을 조회할 수 없습니다." }
        val candidates = findCandidates(type)
        val key =
            if (qualifier != null) {
                DependencyKey(type, qualifier)
            } else {
                check(candidates.size <= 1) {
                    val qualifiers =
                        candidates.keys.joinToString { candidate ->
                            candidate.qualifier?.qualifiedName ?: "Qualifier 없음"
                        }
                    "${type.qualifiedName}에 여러 의존성이 등록되어 있습니다. " +
                        "등록된 Qualifier: $qualifiers. Qualifier를 지정해주세요."
                }
                candidates.keys.singleOrNull() ?: DependencyKey(type, null)
            }
        val owner =
            requireNotNull(candidates[key]) {
                "${type.qualifiedName}에 ${qualifier?.qualifiedName ?: "Qualifier 없음"}로 등록된 의존성이 현재 스코프와 부모 스코프에 없습니다."
            }

        return type.cast(owner.resolveRegistered(key))
    }

    private fun findCandidates(type: KClass<*>): Map<DependencyKey, Scope> {
        val candidates = parent?.findCandidates(type)?.toMutableMap() ?: mutableMapOf()
        factories.keys
            .filter { it.type == type }
            .forEach { key -> candidates[key] = this }
        return candidates
    }

    private fun resolveRegistered(key: DependencyKey): Any =
        instances.getOrPut(key) {
            val factory = checkNotNull(factories[key])
            factory(this).also { inject(it) }
        }

    private fun inject(instance: Any) {
        instance::class
            .declaredMemberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .filter { it.findAnnotation<MyInject>() != null }
            .forEach { property ->
                val dependencyType =
                    requireNotNull(property.returnType.classifier as? KClass<*>) {
                        "${property.name}의 타입을 확인할 수 없습니다."
                    }

                val qualifier =
                    property.annotations
                        .find { annotation ->
                            annotation.annotationClass.findAnnotation<Qualifier>() != null
                        }?.annotationClass

                property.setter.call(instance, resolve(dependencyType, qualifier))
            }
    }

    internal fun <T : Any> create(type: KClass<T>): T {
        val constructor =
            requireNotNull(type.primaryConstructor) {
                "${type.qualifiedName}의 주 생성자를 찾을 수 없습니다."
            }

        val dependencies =
            constructor.parameters.map { parameter ->
                val dependencyType =
                    requireNotNull(parameter.type.classifier as? KClass<*>) {
                        "${parameter.name}의 타입을 확인할 수 없습니다."
                    }

                val qualifier =
                    parameter.annotations
                        .find { annotation ->
                            annotation.annotationClass.findAnnotation<Qualifier>() != null
                        }?.annotationClass

                resolve(dependencyType, qualifier)
            }

        return constructor.call(*dependencies.toTypedArray())
    }
}
