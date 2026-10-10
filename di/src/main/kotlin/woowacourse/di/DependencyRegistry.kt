package woowacourse.di

import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

internal sealed interface Registration {
    data class Binding(
        val implementation: KClass<*>,
    ) : Registration

    data class Instance(
        val value: Any,
    ) : Registration
}

internal class DependencyRegistry {
    private val registrations = mutableMapOf<DependencyKey, Registration>()
    private val resolvedTypes = mutableSetOf<KClass<*>>()

    fun <T : Any> registerInstance(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>?,
    ) {
        val key = DependencyKey(type, qualifier)
        checkCanRegister(key)
        registrations[key] = Registration.Instance(instance)
    }

    fun <T : Any> registerBinding(
        from: KClass<T>,
        to: KClass<out T>,
        qualifier: KClass<out Annotation>?,
    ) {
        val key = DependencyKey(from, qualifier)
        checkCanRegister(key)
        registrations[key] = Registration.Binding(to)
    }

    fun selectKey(
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
                    candidates.joinToString { it.qualifier?.simpleName ?: "Qualifier 미지정" },
            )
        }
    }

    fun registrationFor(key: DependencyKey): Registration? = registrations[key]

    fun registeredInstance(key: DependencyKey): Any? = (registrations[key] as? Registration.Instance)?.value

    fun markResolved(type: KClass<*>) {
        resolvedTypes += type
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
}
