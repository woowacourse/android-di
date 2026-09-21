package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.full.primaryConstructor

class DIContainer {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()

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
            val constructor =
                requireNotNull(type.primaryConstructor) {
                    "${type.simpleName}의 주 생성자를 찾을 수 없습니다."
                }
            val dependencies =
                constructor.parameters.map { parameter ->
                    val dependencyType =
                        requireNotNull(parameter.type.classifier as? KClass<*>) {
                            "${parameter.name}의 타입을 확인할 수 없습니다."
                        }
                    get(dependencyType, resolving)
                }

            return constructor.call(*dependencies.toTypedArray()).also { instance ->
                if (instance !is ViewModel) {
                    instances[type] = instance
                }
            }
        } finally {
            resolving.removeAt(resolving.lastIndex)
        }
    }
}
