package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DIContainer {
    fun <T : Any> get(type: KClass<T>): T {
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
                get(dependencyType)
            }

        return constructor.call(*dependencies.toTypedArray())
    }
}
