package woowacourse.shopping.data.di

import java.lang.IllegalArgumentException
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

object Injector {
    var modules: List<Module> = listOf()

    inline fun <reified T : Any> inject(): T {
        val constructor = T::class.primaryConstructor ?: throw IllegalArgumentException()
        val dependencies:List<Any?> = provideDependencies<T>()
        return constructor.call(*dependencies.toTypedArray())
    }

    inline fun <reified T : Any> provideDependencies(): List<Any?> {
        val constructorParametersType =
            T::class.primaryConstructor?.parameters?.map { it.type } ?: mutableListOf()
        val dependencies: List<Any?> = modules.flatMap { module ->
            findDependencies(module, constructorParametersType)
        }
        return dependencies
    }

    fun findDependencies(
        module: Module,
        constructorParametersType: List<KType>
    ): List<Any?> {
        return constructorParametersType.map { type ->
            module::class.declaredMemberProperties.first {
                it.returnType == type }
        }.map {
            it.call(module) ?: throw NoSuchElementException() }
    }
}