package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object DependencyInjector {
    inline fun <reified T : Any> inject(): T {
        val constructor = T::class.primaryConstructor
            ?: throw IllegalArgumentException()
        // 이후 생성자가 없을 경우 처리 필요
        val arguments = constructor.parameters.map { parameter ->
            val type: KClass<*> = parameter.type.jvmErasure
            DependencyContainer.repositories[type]
        }

        return constructor.call(*arguments.toTypedArray())
    }
}
