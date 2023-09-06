package woowacourse.shopping.di.inject

import woowacourse.shopping.di.DataModule
import woowacourse.shopping.di.Module
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class AutoDependencyInjector(
    private val module: Module,
) {
    fun <T : Any> inject(clazz: Class<T>): T {
        return matchParameterType(clazz.kotlin)
    }

    private fun <T : Any> matchParameterType(clazz: KClass<T>): T {
        val arguments = mutableListOf<Any>()
        val constructor = clazz.primaryConstructor
            ?: throw IllegalArgumentException(INJECT_ERROR_MESSAGE)
        val modules = module::class.declaredMemberProperties

        constructor.parameters.forEach { parameter ->
            val argument = modules.firstOrNull { it.returnType == parameter.type }
                ?: throw IllegalArgumentException(MATCH_MODULE_ERROR_MESSAGE)
            arguments.add(argument.getter.call(DataModule)!!)
        }
        return constructor.call(*arguments.toTypedArray())
    }

    companion object {
        private const val MATCH_MODULE_ERROR_MESSAGE = "필요한 모듈이 존재하지 않습니다."
        private const val INJECT_ERROR_MESSAGE = "주입 할 생성자가 존재하지 않습니다."
    }
}
