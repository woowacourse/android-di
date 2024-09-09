package woowacourse.shopping.shoppingapp.di

import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.jvmErasure

class AppModule(modules: List<KClass<*>>) {
    private val instances = mutableMapOf<KClass<*>, Any>()

    init {
        modules.forEach { module ->
            initInstances(module)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(
        kClass: KClass<T>,
        savedStateHandle: SavedStateHandle? = null,
    ): T {
        return instances[kClass] as? T ?: createInstance(kClass, savedStateHandle)
    }

    private fun initInstances(module: KClass<*>) {
        val provider = createInstance(module)
        val functions = module.declaredFunctions

        functions.forEach { function ->
            val returnType = function.returnType.jvmErasure
            val instance = function.call(provider)
            instances[returnType] = instance
                ?: throw IllegalArgumentException("인스턴스를 생성하지 못했습니다: ${returnType.simpleName}")
        }
    }

    fun <T : Any> createInstance(kClass: KClass<T>): T {
        val constructor =
            kClass.constructors.firstOrNull()
                ?: throw IllegalArgumentException("${kClass.simpleName} 클래스에는 사용할 수 있는 생성자가 없습니다")

        val params =
            constructor.parameters.map { parameter ->
                instances[parameter.type.jvmErasure]
                    ?: throw IllegalArgumentException("필요한 인스턴스가 없습니다: ${parameter.type.jvmErasure.simpleName}")
            }.toTypedArray()

        val instance = constructor.call(*params)
        instances[kClass] = instance
        return instance
    }

    private fun <T : Any> createInstance(
        kClass: KClass<T>,
        savedStateHandle: SavedStateHandle? = null,
    ): T {
        savedStateHandle?.let { instances[SavedStateHandle::class] = it }
        return createInstance(kClass)
    }

    companion object {
        private var instance: AppModule? = null

        fun setInstance(modules: List<KClass<*>>) {
            instance = AppModule(modules)
        }

        fun getInstance(): AppModule {
            return requireNotNull(instance) { "AppModule 인스턴스가 초기화되지 않았습니다" }
        }
    }
}
