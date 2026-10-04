package woowacourse.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DependencyContainer {
    internal data class DependencyKey(
        val type: KClass<*>,
        val qualifier: KClass<out Annotation>?,
    )

    private val applicationInstances = mutableMapOf<DependencyKey, Any>()
    private val scopes = mutableMapOf<String, DependencyScope>()
    val applicationScope = DependencyScope("application", applicationInstances)

    fun openScope(
        id: String,
        onClose: () -> Unit = {},
    ): DependencyScope =
        scopes.getOrPut(id) {
            DependencyScope(id, mutableMapOf(), onClose) {
                scopes.remove(id)
            }
        }

    fun create(
        type: KClass<*>,
        scope: DependencyScope = applicationScope,
    ): Any {
        scope.checkOpen()
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
                    qualifier = qualifier,
                    scope = scope,
                )
            }
        val instance = constructor.call(*args.toTypedArray())
        injectFields(instance, scope)
        return instance
    }

    fun getInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
        scope: DependencyScope = applicationScope,
    ): Any {
        scope.checkOpen()
        val key = DependencyKey(type, qualifier)

        scope.find(key)?.let { return it }

        val qualifiedInstances =
            allRegisteredKeys(scope).filter { dependencyKey ->
                dependencyKey.type == type && dependencyKey.qualifier != null
            }

        if (qualifier == null && qualifiedInstances.isNotEmpty()) {
            throw IllegalArgumentException(
                "${type.simpleName} 타입에는 Qualifier가 필요합니다.",
            )
        }

        if (qualifier != null) {
            throw IllegalArgumentException(
                "${type.simpleName} 타입에 ${qualifier.simpleName} Qualifier가 등록되지 않았습니다.",
            )
        }

        val objectInstance = type.objectInstance
        if (objectInstance != null) return objectInstance

        return scope.instances.getOrPut(key) {
            create(type, scope)
        }
    }

    fun injectFields(
        instance: Any,
        scope: DependencyScope = applicationScope,
    ) {
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
                    qualifier = qualifier,
                    scope = scope,
                )

            field.isAccessible = true
            field.set(instance, dependency)
        }
    }

    fun register(
        type: KClass<*>,
        instance: Any,
    ) {
        register(type, null, instance)
    }

    fun register(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
        instance: Any,
        scope: DependencyScope = applicationScope,
    ) {
        scope.checkOpen()
        scope.instances[DependencyKey(type, qualifier)] = instance
    }

    private fun allRegisteredKeys(scope: DependencyScope): Set<DependencyKey> =
        scope.instances.keys + applicationInstances.keys

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

class DependencyScope internal constructor(
    val id: String,
    internal val instances: MutableMap<DependencyContainer.DependencyKey, Any>,
    private val onClose: () -> Unit = {},
    private val onRemoved: () -> Unit = {},
) {
    var isClosed: Boolean = false
        private set

    fun close() {
        if (isClosed) return
        instances.clear()
        isClosed = true
        onClose()
        onRemoved()
    }

    internal fun checkOpen() {
        check(!isClosed) { "'$id' 스코프가 이미 종료되었습니다." }
    }

    internal fun find(key: DependencyContainer.DependencyKey): Any? =
        instances[key] ?: DependencyContainer.applicationScope.instances[key]
}
