package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object DependencyInjector {
    lateinit var repositoryDependency: RepositoryDependency

    inline fun <reified T : ViewModel> inject(): ViewModelProvider.Factory {
        val clazz = T::class
        val instance = clazz.primaryConstructor!!.parameters.map { params ->
            val paramType = params.type.jvmErasure
            repositoryDependency::class.declaredMemberProperties.find {
                it.returnType.jvmErasure == paramType
            }?.getter?.call(repositoryDependency)
        }

        return viewModelFactory {
            initializer {
                clazz.primaryConstructor!!.call(*instance.toTypedArray())
            }
        }
    }
}
