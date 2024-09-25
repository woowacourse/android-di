package com.kmlibs.supplin.viewmodel

import androidx.lifecycle.ViewModel
import com.kmlibs.supplin.Injector
import com.kmlibs.supplin.application.ApplicationScopeContainer
import com.kmlibs.supplin.base.ComponentContainer
import kotlin.reflect.KClass
import kotlin.reflect.KType

class ViewModelScopeContainer private constructor(
    private val viewModel: ViewModel,
    vararg modules: KClass<*>,
) : ComponentContainer(*modules) {
    init {
        viewModel.addCloseable {
            clearDependencies()
        }
    }

    private fun clearDependencies() {
        containers.remove(viewModel::class)
        Injector.setModules { removeModuleByComponent(viewModel::class) }
    }

    override fun resolveInstance(
        returnType: KType,
        annotations: List<Annotation>,
    ): Any {
        val qualifiedType = buildQualifiedType(returnType, annotations)
        return instances[qualifiedType] ?: buildInstanceOf(qualifiedType)
            ?: ApplicationScopeContainer.container.resolveInstance(returnType, annotations)
    }

    companion object {
        private val containers =
            mutableMapOf<KClass<out ViewModel>, ViewModelScopeContainer>()

        fun <T : ViewModel> containerOf(
            viewModel: T,
            vararg modules: KClass<*>,
        ): ViewModelScopeContainer {
            return containers.getOrPut(viewModel::class) {
                ViewModelScopeContainer(viewModel, *modules)
            }
        }
    }
}
