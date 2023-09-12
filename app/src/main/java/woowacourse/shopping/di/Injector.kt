package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

class Injector(
    private val module: Module,
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

    private fun searchFunctions(kClass: KClass<*>): List<KFunction<*>> =
        module::class.declaredFunctions
            .filter { it.visibility == KVisibility.PUBLIC }
            .filter { it.returnType.jvmErasure == kClass }

    private fun searchFunction(kClass: KClass<*>): KFunction<*> =
        module::class.declaredFunctions
            .filter { it.visibility == KVisibility.PUBLIC }
            .find { it.returnType.jvmErasure == kClass }
            ?: throw IllegalArgumentException(ERROR_NON_EXIST_FUNCTION)

    private fun findInstance(kClass: KClass<*>): Any {
        val instances = searchFunctions(kClass)
        return when (instances.size) {
            0 -> {
                createInstanceForPrimaryConstructor(kClass)
            }

            1 -> {
                val moduleFunction = searchFunction(kClass)
                createInstanceForFunction(moduleFunction)
            }

            else -> {
                throw IllegalStateException(ERROR_CAN_NOT_INJECT)
            }
        }
    }

    private fun createInstanceForPrimaryConstructor(kClass: KClass<*>): Any {
        val primaryConstructor = kClass.primaryConstructor ?: throw NullPointerException(
            ERROR_PRIMARY_CONSTRUCTOR,
        )
        val arguments = getInstances(primaryConstructor)
        return primaryConstructor.call(*arguments.toTypedArray())
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> createInstanceForFunction(kFunction: KFunction<*>): T {
        val arguments = getInstances(kFunction)
        return kFunction.call(module, *arguments.toTypedArray()) as T
    }

    companion object {
        private const val ERROR_PRIMARY_CONSTRUCTOR = "[ERROR] 주 생성자가 없습니다."
        private const val ERROR_NON_EXIST_FUNCTION = "[ERROR] 존재하지 않는 함수입니다."
        private const val ERROR_CAN_NOT_INJECT = "[ERROR] 주입할 수 없습니다."
    }
}
