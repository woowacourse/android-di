package com.kmlibs.supplin.viewmodel

import androidx.lifecycle.ViewModel
import com.kmlibs.supplin.application.ApplicationScopeContainer
import com.kmlibs.supplin.base.ComponentContainer
import com.kmlibs.supplin.model.QualifiedContainerType
import kotlin.reflect.KClass
import kotlin.reflect.KType

class ViewModelScopeContainer private constructor(
    viewModel: ViewModel,
    module: KClass<*>,
) : ComponentContainer(module) {

    init {
        viewModel.addCloseable {
            clearDependencies()
        }
    }

    private fun clearDependencies() {
        containers.remove(qualifiedContainerType)
    }

    override fun resolveInstance(returnType: KType, annotations: List<Annotation>): Any {
        val qualifiedType = buildQualifiedType(returnType, annotations)
        return instances[qualifiedType] ?: buildInstanceOf(qualifiedType)
        ?: ApplicationScopeContainer.container.resolveInstance(returnType, annotations)
    }

    companion object {
        private val containers =
            mutableMapOf<QualifiedContainerType, ViewModelScopeContainer>()

        private lateinit var qualifiedContainerType: QualifiedContainerType

        fun <T : ViewModel> containerOf(
            viewModel: T,
            module: KClass<*>
        ): ViewModelScopeContainer {
            qualifiedContainerType = QualifiedContainerType(module, viewModel::class.simpleName)
            return containers.getOrPut(qualifiedContainerType) {
                ViewModelScopeContainer(viewModel, module)
            }
        }
    }
}
