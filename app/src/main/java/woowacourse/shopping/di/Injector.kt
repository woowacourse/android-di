package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

class Injector(
    private val module: SingletonModule,
) {
    fun <T : Any> inject(clazz: Class<T>): T {
        val primaryConstructor =
            clazz.kotlin.primaryConstructor ?: throw NullPointerException(ERROR_PRIMARY_CONSTRUCTOR)

        val instances = getInstances(primaryConstructor)
        return primaryConstructor.call(*instances.toTypedArray())
    }

    private fun getInstances(kFunction: KFunction<*>): List<Any?> =
        kFunction.valueParameters.map { kParameter ->
            val instance = findInstance(kParameter.type.jvmErasure)
            instance
        }

    private fun searchInstance(kClass: KClass<*>): KFunction<*> =
        module::class.declaredFunctions
            .filter { it.visibility == KVisibility.PUBLIC }
            .find { it.returnType.jvmErasure == kClass } ?: throw IllegalArgumentException(
            ERROR_NON_EXIST_FUNCTION,
        )

    private fun findInstance(kClass: KClass<*>): Any? {
        val constructor = kClass.primaryConstructor ?: run {
            searchInstance(kClass)
        }

        if (constructor.valueParameters.isEmpty()) {
            return searchInstance(kClass).call(module) ?: constructor.call()
        }
        val arguments =
            constructor.valueParameters.map { findInstance(it.type.jvmErasure) }.toTypedArray()
        return constructor.call(module, *arguments)
    }

    companion object {
        private const val ERROR_PRIMARY_CONSTRUCTOR = "[ERROR] 주 생성자가 없습니다."
        private const val ERROR_NON_EXIST_FUNCTION = "[ERROR] 존재하지 않는 함수입니다."
    }
}
