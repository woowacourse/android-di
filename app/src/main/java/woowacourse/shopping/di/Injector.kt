package woowacourse.shopping.di

import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.primaryConstructor

class Injector(
    private val module: SingletonModule,
) {
    private val functions: Map<KType, *> by lazy {
        module::class.declaredFunctions
            .filter { it.visibility == KVisibility.PUBLIC }
            .associate { kFunction ->
                kFunction.returnType to kFunction.call(module)
            }
    }

    fun <T : Any> inject(clazz: Class<T>): T {
        val primaryConstructor =
            clazz.kotlin.primaryConstructor ?: throw NullPointerException(ERROR_PRIMARY_CONSTRUCTOR)

        val instances = getPrimaryConstructorInstances(primaryConstructor)
        return primaryConstructor.call(*instances)
    }

    private fun <T : Any> getPrimaryConstructorInstances(primaryConstructor: KFunction<T>): Array<Any> =
        primaryConstructor.parameters.map { kParameter ->
            functions[kParameter.type] ?: throw NullPointerException(ERROR_NON_EXIST_FUNCTION)
        }.toTypedArray()

    companion object {
        private const val ERROR_PRIMARY_CONSTRUCTOR = "[ERROR] 주 생성자가 없습니다."
        private const val ERROR_NON_EXIST_FUNCTION = "[ERROR] 존재하지 않는 함수입니다."
    }
}
