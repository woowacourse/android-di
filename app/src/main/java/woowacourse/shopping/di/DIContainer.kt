package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.jvm.kotlin
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

object DIContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun <T : Any> findDependencies(constructor: KFunction<T>): List<Any> {
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }
        return getInstances(types)
    }

    private fun getInstances(types: List<KClass<*>>): List<Any> =
        types.map { type ->
            if (instances.keys.contains(type)) {
                instances[type]!!
            } else {
                val constructor = type.primaryConstructor!!
                val instance = constructor.call()
                instances[type] = instance
                instances[type]!!
            }
        }
}

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.kotlin.primaryConstructor!!
        val dependencies = DIContainer.findDependencies(constructor)
        return constructor.call(*dependencies.toTypedArray())
    }
}
