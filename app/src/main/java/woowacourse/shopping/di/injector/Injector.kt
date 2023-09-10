package woowacourse.shopping.di.injector

import woowacourse.shopping.di.module.Module
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class Injector(private val modules: List<Module>) {

    constructor(vararg modules: Module) : this(modules.toList())

    private val providers: MutableMap<KType, Any?> =
        mutableMapOf<KType, Any?>().apply {
            modules.forEach { module ->
                module::class.declaredMemberFunctions.forEach {
                    val returnType = it.returnType
                    val instance = it.call(module)
                    this[returnType] = instance
                }
            }
        }

    inline fun <reified T : Any> inject(): T {
        val primaryConstructor = T::class.primaryConstructor
            ?: throw NullPointerException("${T::class.simpleName} 클래스의 주생성자를 가져오는데 실패하였습니다.")
        val dependencies = getDependencies(primaryConstructor.parameters.map { it.type })
        val instance = primaryConstructor.call(*dependencies.toTypedArray())
        injectProperties(instance)
        return instance
    }

    fun <T : Any> injectProperties(instance: T) {
        val mutableProperties =
            instance::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
        mutableProperties.forEach { property ->
            if (property.annotations.any { it is Inject }.not()) return@forEach
            val dependency = providers[property.returnType]
                ?: throw NullPointerException("${property.name}에 대한 의존성을 가져오는데 실피하였습니다.")
            property.isAccessible = true
            property.setter.call(instance, dependency)
        }
    }

    fun getDependencies(dependencyTypes: List<KType>): List<Any> {
        val dependencies = mutableListOf<Any>()

        dependencyTypes.forEach { paramType ->
            val instance = providers[paramType]
            instance?.let { dependencies.add(instance) }
        }

        return dependencies
    }
}
