package woowacourse.di

import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndIncrement
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DependencyContainer {
    internal data class DependencyKey(
        val type: KClass<*>,
        val qualifier: KClass<out Annotation>?,
    )

    @OptIn(ExperimentalAtomicApi::class)
    private val nextScopeId = AtomicLong(2L)
    private val scopes = mutableMapOf<Long, DependencyScope>()

    fun openScope(id: Long, parentId: Long? = null): DependencyScope {
        check(id !in scopes) { "이미 열린 스코프입니다: $id" }

        val parent = parentId?.let { scopes.getValue(parentId) }
        return DependencyScope(parent).also { scope ->
            scopes[id] = scope
        }
    }

    @OptIn(ExperimentalAtomicApi::class)
    fun openChildScope(parentId: Long): Long {
        val id = nextScopeId.fetchAndIncrement()
        openScope(id, parentId)
        return id
    }

    fun closeScope(id: Long) {
        scopes.remove(id)?.close()
    }

    fun create(type: KClass<*>, scopeId: Long): Any {
        val objectInstance = type.objectInstance
        if (objectInstance != null) return objectInstance

        val constructor =
            type.primaryConstructor
                ?: throw IllegalArgumentException(
                    "${type.simpleName}에 primary constructor가 없습니다.",
                )
        val parameters = constructor.parameters
        val args =
            parameters.map { parameter ->
                val dependencyClass =
                    parameter.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException(
                            "${type.simpleName}의 ${parameter.name} 파라미터 타입을 확인할 수 없습니다.",
                        )

                val qualifier = findQualifier(parameter.annotations)

                getInstance(
                    type = dependencyClass,
                    scopeId = scopeId,
                    qualifier = qualifier,
                )
            }
        val instance = constructor.call(*args.toTypedArray())
        injectFields(instance, scopeId)
        return instance
    }

    fun getInstance(
        type: KClass<*>,
        scopeId: Long,
        qualifier: KClass<out Annotation>? = null,
    ): Any {
        val scope = scopes.getValue(scopeId)
        val key = DependencyKey(type, qualifier)


        scope.find(key)?.let { return it }

        if (qualifier == null && scope.hasQualifiedBinding(type)) {
            throw IllegalArgumentException(
                "${type.simpleName} 타입에는 Qualifier가 필요합니다."
            )
        }

        if (qualifier != null) {
            throw IllegalArgumentException(
                "${type.simpleName} 타입에 ${qualifier.simpleName} Qualifier가 등록되지 않았습니다."
            )
        }

        val objectInstance = type.objectInstance
        if (objectInstance != null) return objectInstance

        return scope.getOrCreate(key) {
            create(type, scopeId)
        }
    }

    fun injectFields(instance: Any, scopeId: Long) {
        val fields = instance.javaClass.declaredFields
        fields.forEach { field ->
            if (!field.isAnnotationPresent(Inject::class.java)) return@forEach

            val qualifier =
                field.annotations
                    .map { it.annotationClass.java }
                    .firstOrNull { annotationClass ->
                        annotationClass.isAnnotationPresent(Qualifier::class.java)
                    }?.kotlin

            val dependency =
                getInstance(
                    type = field.type.kotlin,
                    scopeId = scopeId,
                    qualifier = qualifier,
                )

            field.isAccessible = true
            field.set(instance, dependency)
        }
    }

    fun register(
        scopeId: Long,
        type: KClass<*>,
        instance: Any,
    ) {
        register(scopeId, type, null, instance)
    }

    fun register(
        scopeId: Long,
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
        instance: Any,

    ) {
        val key = DependencyKey(type, qualifier)
        scopes.getValue(scopeId).register(key, instance)
    }

    private fun findQualifier(annotations: Iterable<Annotation>): KClass<out Annotation>? {
        val qualifiers =
            annotations.filter { annotation ->
                annotation.annotationClass.java
                    .isAnnotationPresent(Qualifier::class.java)
            }

        if (qualifiers.size > 1) {
            throw IllegalArgumentException("하나의 필드 또는 파라미터에는 Qualifier를 하나만 지정해야 합니다.")
        }

        return qualifiers.firstOrNull()?.annotationClass
    }
}
