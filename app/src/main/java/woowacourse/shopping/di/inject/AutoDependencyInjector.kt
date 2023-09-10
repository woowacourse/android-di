package woowacourse.shopping.di.inject

import woowacourse.shopping.di.Container
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object AutoDependencyInjector {
    private const val INJECT_ERROR_MESSAGE = "주입 할 생성자가 존재 하지 않습니다."

    inline fun <reified T : Any> inject(clazz: KClass<*>): T {
        var instance = Container.getInstance(clazz)
        if (instance == null) instance = createInstance(clazz)
        return instance as T
    }

    fun <T : Any> createInstance(clazz: KClass<*>): T {
        val constructor = clazz.primaryConstructor
            ?: throw IllegalArgumentException(INJECT_ERROR_MESSAGE)
        val arguments = constructor.parameters.map { parameter ->
            val type = parameter.type.jvmErasure
            Container.getInstance(type) ?: inject(type)
        }
        return constructor.call(*arguments.toTypedArray()) as T
    }

//    fun <T : Any> inject(clazz: Class<T>): T {
//        return matchParameterType(clazz.kotlin)
//    }
//
//    private fun <T : Any> matchParameterType(clazz: KClass<T>): T {
//        val arguments = mutableListOf<Any>()
//        val constructor = clazz.primaryConstructor
//            ?: throw IllegalArgumentException(INJECT_ERROR_MESSAGE)
//        val modules = module::class.declaredMemberProperties
//
//        constructor.parameters.forEach { parameter ->
//            val argument = modules.firstOrNull { it.returnType == parameter.type }
//                ?: throw IllegalArgumentException(MATCH_MODULE_ERROR_MESSAGE)
//            arguments.add(argument.getter.call(DataModule)!!)
//        }
//        return constructor.call(*arguments.toTypedArray())
//    }
}
