package woowacourse.shopping.data.di

import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

object Injector {
    var modules: List<Module> = listOf()

    inline fun <reified T : Any> inject(): T {
        val constructor = requireNotNull(T::class.primaryConstructor)
        val dependencies: List<Any> = provideDependencies(constructor)
        return constructor.call(*dependencies.toTypedArray())
    }

    inline fun <reified T : Any> provideDependencies(constructor: KFunction<T>): List<Any> {
        val constructorParametersType = constructor.parameters.map { it.type }
        val dependencies: List<Any> = modules.flatMap { module ->
            findDependencies(module, constructorParametersType)
        }
        return dependencies
    }

    fun findDependencies(
        module: Module, constructorParametersType: List<KType>
    ): List<Any> {
        val foundProperties: List<KProperty<*>> = constructorParametersType.map { type ->
            module::class.declaredMemberProperties.first {
                it.returnType == type
            }
        }
        return foundProperties.map {
            // 프로퍼티 값 가져오기
            it.call(module) ?: throw NoSuchElementException()
        }
    }
}