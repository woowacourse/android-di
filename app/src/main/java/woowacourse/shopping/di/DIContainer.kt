package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import kotlin.jvm.kotlin
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.full.primaryConstructor

class DIContainer {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()
    private val bindings: MutableMap<KClass<*>, KClass<*>> = mutableMapOf()

    fun <T : Any, I : T> bind(
        type: KClass<T>,
        implementation: KClass<I>,
    ) {
        bindings[type] = implementation
    }

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
    ) {
        instances[type] = instance
    }

    fun <T : Any> get(type: KClass<T>): T = get(type, mutableListOf())

    private fun <T : Any> get(
        type: KClass<T>,
        resolving: MutableList<KClass<*>>,
    ): T {
        instances[type]?.let { instance -> return type.cast(instance) }

        val cycleStart = resolving.indexOf(type)
        require(cycleStart == -1) {
            val cycle = (resolving.drop(cycleStart) + type).joinToString(" → ") { it.simpleName.orEmpty() }
            "순환 의존성: $cycle"
        }

        resolving.add(type)
        try {
            val implementationType = bindings[type] ?: type
            val constructor =
                requireNotNull(implementationType.primaryConstructor) {
                    "${implementationType.simpleName}의 주 생성자를 찾을 수 없습니다."
                }
            val dependencies =
                constructor.parameters.map { parameter ->
                    val dependencyType =
                        requireNotNull(parameter.type.classifier as? KClass<*>) {
                            "${parameter.name}의 타입을 확인할 수 없습니다."
                        }
                    get(dependencyType, resolving)
                }

            val instance = constructor.call(*dependencies.toTypedArray())
            injectFields(instance, resolving)
            if (instance !is ViewModel) {
                instances[type] = instance
                instances[implementationType] = instance
            }
            return type.cast(instance)
        } finally {
            resolving.removeAt(resolving.lastIndex)
        }
    }

    private fun injectFields(
        instance: Any,
        resolving: MutableList<KClass<*>>,
    ) {
        instance.javaClass.declaredFields
            .filter { field -> field.isAnnotationPresent(Inject::class.java) }
            .forEach { field ->
                field.isAccessible = true
                field.set(instance, get(field.type.kotlin, resolving))
            }
    }
}
