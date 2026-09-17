package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object HunnitFactory : ViewModelProvider.Factory {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()

    override fun <T : ViewModel> create(modelClass: Class<T>): T = createInstance(modelClass.kotlin)

    fun <T : Any> createInstance(targetClass: KClass<T>): T {
        val constructor = targetClass.primaryConstructor!!
        val parameterTypes = constructor.parameters.map { it.type.classifier as KClass<*> }

        val instance = constructor.call(*parameterTypes.map { getInstance(it) }.toTypedArray())

        return instance
    }

    fun getInstance(kClass: KClass<*>): Any {
        instances[kClass]?.let { return it }

        val newInstance = createInstance(kClass)
        instances[kClass] = newInstance

        return newInstance
    }
}
