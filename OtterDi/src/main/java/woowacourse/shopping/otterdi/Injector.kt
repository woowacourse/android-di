package woowacourse.shopping.otterdi

import woowacourse.shopping.otterdi.annotation.Inject
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class Injector(val dependencies: Dependencies) {
    constructor(vararg modules: Module) : this(Dependencies(modules.toList()))

    inline fun <reified T : Any> inject(): T {
        val primaryConstructor = T::class.primaryConstructor
            ?: throw NullPointerException("${T::class.simpleName} 클래스의 주생성자를 가져오는데 실패하였습니다. 인터페이스와 같이 주생성자가 없는 객체인지 확인해주세요.")
        val injectParams: List<KParameter> = getInjectParams(primaryConstructor)
        val args = dependencies.getInstances(injectParams)
        val instance = primaryConstructor.call(*args.toTypedArray())

        injectProperties(instance)
        return instance
    }

    fun getInjectParams(constructor: KFunction<*>): List<KParameter> {
        if (constructor.hasAnnotation<Inject>()) return constructor.parameters
        return constructor.parameters.filter { it.hasAnnotation<Inject>() }
    }

    fun <T : Any> injectProperties(instance: T) {
        val mutableProperties =
            instance::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
        mutableProperties.forEach { property ->
            if (property.annotations.any { it is Inject }.not()) return@forEach
            val dependency = dependencies.getInstance(property)
            property.isAccessible = true
            property.setter.call(instance, dependency)
        }
    }
}
