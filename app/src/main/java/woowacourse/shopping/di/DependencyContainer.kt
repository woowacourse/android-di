package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

internal class DependencyContainer(
    dependencies: Map<KClass<*>, Any> = emptyMap(),
    private val bindings: Map<KClass<*>, KClass<*>> = emptyMap(),
) {
    private val dependencies: MutableMap<KClass<*>, Any> = dependencies.toMutableMap()

    fun register(
        type: KClass<*>,
        create: () -> Any,
    ) {
        dependencies.getOrPut(type, create)
    }

    fun create(type: KClass<*>): Any {
        val constructor = type.primaryConstructor ?: error("주 생성자를 찾을 수 없습니다: ${type.qualifiedName}")
        constructor.isAccessible = true
        val arguments =
            constructor.parameters.associateWith { parameter ->
                resolve(parameter.type.jvmErasure)
            }
        return constructor.callBy(arguments)
    }

    fun resolve(type: KClass<*>): Any =
        dependencies.getOrPut(type) {
            create(bindings[type] ?: type)
        }
}
