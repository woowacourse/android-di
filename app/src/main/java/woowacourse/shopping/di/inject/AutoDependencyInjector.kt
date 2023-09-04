package woowacourse.shopping.di.inject

import woowacourse.shopping.di.DataModule
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class AutoDependencyInjector {
    fun <T : Any> inject(clazz: Class<T>): T {
        return (
            matchParameterType(clazz.kotlin)
                ?: throw IllegalArgumentException(INJECT_ERROR_MESSAGE)
            )
    }

    private fun <T : Any> matchParameterType(clazz: KClass<T>): T? {
        val arguments = mutableListOf<Any?>()
        val constructor = clazz.primaryConstructor
        val modules = DataModule::class.declaredMemberProperties

        constructor?.parameters?.forEach { parameter ->
            val argument = modules.first { it.returnType == parameter.type }
            argument.isAccessible = true
            arguments.add(argument.getter.call(DataModule))
        }
        return constructor?.call(*arguments.toTypedArray())
    }

    companion object {
        private const val INJECT_ERROR_MESSAGE = "주입 할 생성자가 존재하지 않습니다."
    }
}
