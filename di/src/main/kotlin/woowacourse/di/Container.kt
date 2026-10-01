package woowacourse.di

import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class Container {
    private data class Key(
        val type: KClass<*>,
        val qualifier: KClass<out Annotation>?,
    )

    private sealed interface Binding {
        data class Instance(
            val value: Any,
        ) : Binding

        data class Implementation(
            val type: KClass<*>,
        ) : Binding
    }

    private val bindings = mutableMapOf<Key, Binding>()
    private val instances = mutableMapOf<Key, Any>()
    private val resolvingPath = mutableListOf<KClass<*>>()

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        checkQualifier(qualifier)
        val key = Key(type, qualifier)
        bindings[key] = Binding.Instance(instance)
        instances[key] = instance
    }

    fun <T : Any> bind(
        type: KClass<T>,
        implementation: KClass<out T>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        checkQualifier(qualifier)
        val key = Key(type, qualifier)
        bindings[key] = Binding.Implementation(implementation)
        instances.remove(key)
    }

    fun <T : Any> create(type: KClass<T>): T {
        check(type !in resolvingPath) {
            "의존성: ${(resolvingPath + type).joinToString(" - ") { it.simpleName ?: it.toString() }}"
        }
        resolvingPath.add(type)
        try {
            val constructor =
                type.primaryConstructor
                    ?: throw IllegalArgumentException("$type: 주 생성자를 찾을 수 없습니다.")
            val arguments =
                constructor.parameters.map { parameter ->
                    val parameterType =
                        parameter.type.classifier as? KClass<*>
                            ?: throw IllegalArgumentException("$type: 생성자 매개변수 타입을 확인할 수 없습니다.")
                    resolve(parameterType, qualifierOf(parameter.annotations))
                }
            val instance = constructor.call(*arguments.toTypedArray())

            type.java.declaredFields
                .filter { it.isAnnotationPresent(Inject::class.java) }
                .forEach { field ->
                    field.isAccessible = true
                    field.set(instance, resolve(field.type.kotlin, qualifierOf(field.annotations.asIterable())))
                }
            return instance
        } finally {
            resolvingPath.removeAt(resolvingPath.lastIndex)
        }
    }

    fun resolve(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any {
        checkQualifier(qualifier)
        val key = bindingKey(type, qualifier)
        return instances.getOrPut(key) {
            when (val binding = bindings[key]) {
                is Binding.Instance -> binding.value
                is Binding.Implementation -> create(binding.type)
                null -> {
                    if (type.java.isInterface || Modifier.isAbstract(type.java.modifiers)) {
                        throw IllegalArgumentException("$type: 등록된 구현체가 없습니다.")
                    }
                    create(type)
                }
            }
        }
    }

    private fun bindingKey(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ): Key {
        if (qualifier != null) {
            val key = Key(type, qualifier)
            require(key in bindings) { "$type: ${qualifier.simpleName} Qualifier에 등록된 구현체가 없습니다." }
            return key
        }

        val candidates = bindings.keys.filter { it.type == type }
        require(candidates.size <= 1) { "$type: 구현체가 여러 개입니다. Qualifier를 지정해야 합니다." }
        return candidates.singleOrNull() ?: Key(type, null)
    }

    private fun qualifierOf(annotations: Iterable<Annotation>): KClass<out Annotation>? {
        val qualifiers =
            annotations
                .map { it.annotationClass }
                .filter { annotation -> annotation.annotations.any { it is Qualifier } }
        require(qualifiers.size <= 1) { "주입 대상에 Qualifier를 둘 이상 지정할 수 없습니다." }
        return qualifiers.singleOrNull()
    }

    private fun checkQualifier(qualifier: KClass<out Annotation>?) {
        require(qualifier == null || qualifier.annotations.any { it is Qualifier }) {
            "$qualifier: @Qualifier 애노테이션이 필요합니다."
        }
    }
}
