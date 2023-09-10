package woowacourse.shopping.data.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object Injector {
    lateinit var container: Container
    const val NO_INSTANCE_ERROR = "해당하는 인스턴스를 찾을 수 없습니다."

    inline fun <reified T : Any> inject(): T {
        val constructor = requireNotNull(T::class.primaryConstructor)
        val dependencies: List<Any> = provideDependencies(constructor)
        return constructor.call(*dependencies.toTypedArray())
    }

    inline fun <reified T : Any> provideDependencies(constructor: KFunction<T>): List<Any> {
        val constructorParametersType: List<KClass<*>> =
            constructor.parameters.map { it.type.jvmErasure }
        val dependencies: List<Any> = constructorParametersType.map {
            container.getInstance(it) ?: throw NoSuchElementException(NO_INSTANCE_ERROR)
        }
        return dependencies
    }
}