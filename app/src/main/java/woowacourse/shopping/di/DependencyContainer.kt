package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DependencyContainer {
    fun <T : Any> resolve(type: KClass<T>): T {
        val constructor =
            requireNotNull(type.primaryConstructor) {
                "${type.qualifiedName}의 주 생성자를 찾을 수 없습니다."
            }

        return constructor.call()
    }
}
