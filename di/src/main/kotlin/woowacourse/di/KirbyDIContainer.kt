package woowacourse.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class KirbyDIContainer {
    private val registry = DependencyRegistry()

    fun <T : Any> registerInstance(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) = registry.registerInstance(type, instance, qualifier)

    fun <T : Any> registerBinding(
        from: KClass<T>,
        to: KClass<out T>,
        qualifier: KClass<out Annotation>? = null,
    ) = registry.registerBinding(from, to, qualifier)

    fun <T : Any> createInstance(type: KClass<T>): T {
        val key = registry.selectKey(type, null)
        val registration = registry.registrationFor(key)
        require(registration !is Registration.Instance) { "등록된 인스턴스를 새로 생성할 수 없습니다: $key" }
        val target = (registration as? Registration.Binding)?.implementation ?: type

        @Suppress("UNCHECKED_CAST")
        return (instantiate(key, target, mutableSetOf()) as T).also { registry.markResolved(type) }
    }

    fun <T : Any> resolve(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        @Suppress("UNCHECKED_CAST")
        return resolveDependency(type, qualifier, mutableSetOf()) as T
    }

    private fun resolveDependency(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
        creatingKeys: MutableSet<DependencyKey>,
    ): Any {
        val key = registry.selectKey(type, qualifier)
        registry.existingInstance(key)?.let {
            registry.markResolved(type)
            return it
        }

        val target = (registry.registrationFor(key) as? Registration.Binding)?.implementation ?: type
        return instantiate(key, target, creatingKeys).also {
            registry.cacheGenerated(key, type, it)
        }
    }

    private fun instantiate(
        key: DependencyKey,
        target: KClass<*>,
        creatingKeys: MutableSet<DependencyKey>,
    ): Any {
        require(key !in creatingKeys) { "순환 의존성이 발견되었습니다: $key" }
        creatingKeys += key
        try {
            val constructor =
                target.primaryConstructor
                    ?: throw IllegalArgumentException("주 생성자가 없습니다: $target")
            val arguments =
                constructor.parameters.map { parameter ->
                    val parameterType =
                        parameter.type.classifier as? KClass<*>
                            ?: throw IllegalArgumentException("의존성 타입을 확인할 수 없습니다: $parameter")
                    resolveDependency(parameterType, qualifierOf(parameter.annotations), creatingKeys)
                }
            val instance = constructor.call(*arguments.toTypedArray())
            injectFields(instance, creatingKeys)
            return instance
        } finally {
            creatingKeys -= key
        }
    }

    private fun injectFields(
        target: Any,
        creatingKeys: MutableSet<DependencyKey>,
    ) {
        target::class
            .memberProperties
            .filterIsInstance<KMutableProperty1<*, *>>()
            .filter { it.findAnnotation<KirbyInject>() != null }
            .forEach { property ->
                property.isAccessible = true
                val dependencyType =
                    property.returnType.classifier as? KClass<*>
                        ?: throw IllegalArgumentException("의존성 타입을 확인할 수 없습니다: $property")
                property.setter.call(target, resolveDependency(dependencyType, qualifierOf(property.annotations), creatingKeys))
            }
    }

    private fun qualifierOf(annotations: List<Annotation>): KClass<out Annotation>? {
        val qualifiers =
            annotations.mapNotNull { annotation ->
                annotation.annotationClass.takeIf { it.findAnnotation<KirbyQualifier>() != null }
            }
        require(qualifiers.size <= 1) { "Qualifier를 둘 이상 지정할 수 없습니다: $qualifiers" }
        return qualifiers.singleOrNull()
    }
}
