package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.full.primaryConstructor

class DependencyContainer {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()

    fun <T : Any> resolve(type: KClass<T>): T {
        val instance =
            instances.getOrPut(type) {
                create(type)
            }

        return type.cast(instance)
    }

    private fun create(type: KClass<*>): Any {
        val constructor =
            requireNotNull(type.primaryConstructor) {
                "${type.qualifiedName}의 주 생성자를 찾을 수 없습니다."
            }

        val dependencies =
            constructor.parameters.map { parameter ->
                val dependencyType =
                    requireNotNull(
                        parameter.type.classifier as? KClass<*>,
                    ) {
                        "${parameter.name}의 타입을 확인할 수 없습니다."
                    }

                resolve(dependencyType)
            }

        return constructor.call(*dependencies.toTypedArray())
    }
}
