package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.jvm.kotlin
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DIContainer {
    val dependencies = mutableListOf<Pair<KClass<*>, Any>>()

    fun findDependencies(types: List<KClass<*>>): List<Any> =
        types.map { type ->
            if (dependencies.map { it.first }.contains(type)) {
                dependencies.find { it.first == type }!!.second
            } else {
                val constructor = type.primaryConstructor!!
                val dependency = constructor.call()
                dependencies.add(type to dependency)
                dependency
            }
        }
}

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.kotlin.primaryConstructor!!
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }
        val dependencies = DIContainer.findDependencies(types)
        return constructor.call(*dependencies.toTypedArray())
    }
}
