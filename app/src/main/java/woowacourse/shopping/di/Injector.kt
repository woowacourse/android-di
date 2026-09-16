package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object Injector {
    fun<T: Any> create(type: KClass<T>): T {
        val constructor = requireNotNull(type.primaryConstructor) {
            "${type.simpleName}의 주 생성자를 찾을 수 없습니다."
        }

        val arguments = constructor.parameters.map { parameter ->
            val dependencyType =
                parameter.type.classifier as? KClass<*>
                    ?: error("지원하지 않는 타입 : ${parameter.type}")

            create(dependencyType)
        }

        return constructor.call(*arguments.toTypedArray())
    }
}