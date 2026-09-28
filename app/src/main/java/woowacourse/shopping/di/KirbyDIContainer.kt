package woowacourse.shopping.di

import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class KirbyDIContainer {
    private sealed interface Registration {
        data class Binding(
            val implementation: KClass<*>,
        ) : Registration

        data class Instance(
            val value: Any,
        ) : Registration
    }

    private val registrations = mutableMapOf<DependencyKey, Registration>()
    private val instances = mutableMapOf<DependencyKey, Any>()
    private val resolvedTypes = mutableSetOf<KClass<*>>()

    fun <T : Any> registerInstance(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        val key = DependencyKey(type, qualifier)
        checkCanRegister(key)
        registrations[key] = Registration.Instance(instance)
        instances[key] = instance
    }

    fun <T : Any> registerBinding(
        from: KClass<T>,
        to: KClass<out T>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        val key = DependencyKey(from, qualifier)
        checkCanRegister(key)
        registrations[key] = Registration.Binding(to)
    }

    fun <T : Any> createInstance(type: KClass<T>): T {
        val key = selectKey(type, null)
        val registration = registrations[key]
        require(registration !is Registration.Instance) { "등록된 인스턴스를 새로 생성할 수 없습니다: $key" }
        val target = (registration as? Registration.Binding)?.implementation ?: type

        @Suppress("UNCHECKED_CAST")
        return (instantiate(key, target, mutableSetOf()) as T).also { resolvedTypes += type }
    }

    fun <T : Any> resolve(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        @Suppress("UNCHECKED_CAST")
        return resolveDependency(type, qualifier, mutableSetOf()) as T
    }

    private fun checkCanRegister(key: DependencyKey) {
        validateQualifier(key.qualifier)
        require(key.type !in resolvedTypes) { "이미 해결한 타입에는 등록할 수 없습니다: ${key.type}" }
        require(key !in registrations) { "같은 타입과 Qualifier가 이미 등록되었습니다: $key" }
    }

    private fun validateQualifier(qualifier: KClass<out Annotation>?) {
        require(qualifier == null || qualifier.findAnnotation<KirbyQualifier>() != null) {
            "@KirbyQualifier가 없는 어노테이션입니다: $qualifier"
        }
    }

    private fun selectKey(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ): DependencyKey {
        if (qualifier != null) {
            validateQualifier(qualifier)
            val key = DependencyKey(type, qualifier)
            require(key in registrations) { "Qualifier에 해당하는 의존성이 등록되지 않았습니다: $key" }
            return key
        }

        val candidates = registrations.keys.filter { it.type == type }
        return when (candidates.size) {
            0 -> {
                require(!type.java.isInterface && !Modifier.isAbstract(type.java.modifiers)) {
                    "구현체가 등록되지 않았습니다: $type"
                }
                DependencyKey(type, null)
            }
            1 -> candidates.single()
            else -> throw IllegalArgumentException(
                "Qualifier 없이 $type 의존성을 선택할 수 없습니다. 후보: " +
                    candidates.joinToString { it.qualifier?.simpleName ?: "무자격" },
            )
        }
    }

    private fun resolveDependency(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
        path: MutableSet<DependencyKey>,
    ): Any {
        val key = selectKey(type, qualifier)
        instances[key]?.let {
            resolvedTypes += type
            return it
        }

        val registration = registrations[key]
        val target =
            when (registration) {
                is Registration.Binding -> registration.implementation
                is Registration.Instance -> {
                    resolvedTypes += type
                    return registration.value
                }
                null -> type
            }
        return instantiate(key, target, path).also {
            instances[key] = it
            resolvedTypes += type
        }
    }

    private fun instantiate(
        key: DependencyKey,
        target: KClass<*>,
        path: MutableSet<DependencyKey>,
    ): Any {
        require(path.add(key)) { "순환 의존성이 발견되었습니다: $key" }
        try {
            val constructor =
                target.primaryConstructor
                    ?: throw IllegalArgumentException("주 생성자가 없습니다: $target")
            val arguments =
                constructor.parameters.map { parameter ->
                    val parameterType =
                        parameter.type.classifier as? KClass<*>
                            ?: throw IllegalArgumentException("의존성 타입을 확인할 수 없습니다: $parameter")
                    resolveDependency(parameterType, qualifierOf(parameter.annotations), path)
                }
            val instance = constructor.call(*arguments.toTypedArray())
            injectFields(instance, path)
            return instance
        } finally {
            path.remove(key)
        }
    }

    private fun injectFields(
        target: Any,
        path: MutableSet<DependencyKey>,
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
                property.setter.call(target, resolveDependency(dependencyType, qualifierOf(property.annotations), path))
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
