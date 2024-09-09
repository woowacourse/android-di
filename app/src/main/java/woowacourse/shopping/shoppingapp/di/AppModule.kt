package woowacourse.shopping.shoppingapp.di

import androidx.lifecycle.SavedStateHandle
import woowacourse.shopping.data.di.RepositoryModule
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.jvmErasure

class AppModule {
    private val instances = mutableMapOf<KClass<*>, Any>()

    init {
        initInstances(RepositoryModule::class.declaredFunctions)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(
        kClass: KClass<T>,
        savedStateHandle: SavedStateHandle? = null,
    ): T {
        return instances[kClass] as? T ?: createInstance(kClass, savedStateHandle)
    }

    private fun initInstances(functions: Collection<KFunction<*>>) {
        functions.forEach { function ->
            val kClass = function.returnType.jvmErasure
            val instance = function.call(RepositoryModule)
            instances[kClass] = instance ?: throw IllegalArgumentException("인스턴스를 생성하지 못했습니다")
        }
    }

    fun <T : Any> createInstance(kClass: KClass<T>): T {
        val constructor = kClass.constructors.first()
        val params =
            constructor.parameters.map { parameter ->
                instances[parameter.type.jvmErasure] ?: throw IllegalArgumentException("인스턴스가 없습니다")
            }.toTypedArray()

        val instance = constructor.call(*params)
        instances[kClass] = instance

        return instance
    }

    private fun <T : Any> createInstance(
        kClass: KClass<T>,
        savedStateHandle: SavedStateHandle? = null,
    ): T {
        if (savedStateHandle != null) {
            instances[savedStateHandle::class] = savedStateHandle
        }

        return createInstance(kClass = kClass)
    }

    companion object {
        private var instance: AppModule? = null

        fun setInstance() {
            instance = AppModule()
        }

        fun getInstance(): AppModule = requireNotNull(instance)
    }
}
