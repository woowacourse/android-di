package woowacourse.shopping.otterdi

import woowacourse.shopping.otterdi.annotation.Inject
import woowacourse.shopping.otterdi.annotation.Qualifier
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class Injector(private val modules: List<Module>) {

    constructor(vararg modules: Module) : this(modules.toList())

    private val providers: MutableMap<String, Any?> = mutableMapOf<String, Any?>().apply {
        modules.forEach { module ->
            module::class.declaredMemberFunctions.forEach {
                val returnType = it.returnType.toString()
                val instance = it.call(module)
                if (it.findAnnotation<Qualifier>() == null) {
                    this[returnType] = instance
                } else {
                    val implementationName =
                        it.findAnnotation<Qualifier>()?.implementationName.toString()
                    this[implementationName] = instance
                }
            }
        }
    }

    inline fun <reified T : Any> inject(): T {
        val primaryConstructor = T::class.primaryConstructor
            ?: throw NullPointerException("${T::class.simpleName} 클래스의 주생성자를 가져오는데 실패하였습니다. 인터페이스와 같이 주생성자가 없는 객체인지 확인해주세요.")
        val injectParams: List<KParameter> =
            primaryConstructor.parameters.filter { it.hasAnnotation<Inject>() }
        val dependencies = getDependencies(getDependenciesTypes(injectParams))
        val instance = primaryConstructor.call(*dependencies.toTypedArray())
        injectProperties(instance)
        return instance
    }

    fun <T : Any> injectProperties(instance: T) {
        val mutableProperties =
            instance::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
        mutableProperties.forEach { property ->
            if (property.annotations.any { it is Inject }.not()) return@forEach
            val dependency = getDependency(property.returnType.toString())
            property.isAccessible = true
            property.setter.call(instance, dependency)
        }
    }

    fun getDependenciesTypes(params: List<KParameter>): List<String> {
        return params.map { param ->
            if (param.findAnnotation<Qualifier>() == null) {
                param.type.toString()
            } else {
                param.findAnnotation<Qualifier>()?.implementationName.toString()
            }
        }
    }

    private fun getDependency(type: String): Any {
        return providers[type] ?: throw IllegalArgumentException("${type}에 대한 의존성을 가져오는데 실피하였습니다.")
    }

    fun getDependencies(dependencyTypes: List<String>): List<Any> {
        return dependencyTypes.map { dependencyType ->
            getDependency(type = dependencyType)
        }
    }
}
