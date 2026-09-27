package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class KirbyDIContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()
    private val bindings = mutableMapOf<KClass<*>, KClass<*>>()
    private val resolving = mutableSetOf<KClass<*>>()

    fun <T : Any> registerInstance(
        type: KClass<T>,
        instance: T,
    ) {
        instances[type] = instance
    }

    fun <T : Any> registerBinding(
        from: KClass<T>,
        to: KClass<out T>,
    ) {
        bindings[from] = to
    }

    fun <T : Any> createInstance(type: KClass<T>): T = instantiate(type)

    fun <T : Any> resolve(type: KClass<T>): T {
        instances[type]?.let {
            @Suppress("UNCHECKED_CAST")
            return it as T
        }
        val instance = instantiate(type)
        instances[type] = instance
        return instance
    }

    private fun <T : Any> instantiate(type: KClass<T>): T {
        require(resolving.add(type)) { "순환 의존성이 발견되었습니다: $type" }
        try {
            val target = bindings[type] ?: type
            val constructor =
                target.primaryConstructor
                    ?: throw IllegalArgumentException("주 생성자가 없습니다: $target")
            val arguments =
                constructor.parameters.map { parameter ->
                    val parameterType =
                        parameter.type.classifier as? KClass<*>
                            ?: throw IllegalArgumentException("의존성 타입을 확인할 수 없습니다: $parameter")
                    resolve(parameterType)
                }

            @Suppress("UNCHECKED_CAST")
            val instance = constructor.call(*arguments.toTypedArray()) as T
            injectFields(instance)
            return instance
        } finally {
            resolving.remove(type)
        }
    }

    private fun injectFields(target: Any) {
        target::class
            .memberProperties
            .filterIsInstance<KMutableProperty1<*, *>>()
            .filter { it.findAnnotation<KirbyInject>() != null }
            .forEach { property ->
                property.isAccessible = true
                val dependencyType = property.returnType.classifier as KClass<*>
                property.setter.call(target, resolve(dependencyType))
            }
    }
}
