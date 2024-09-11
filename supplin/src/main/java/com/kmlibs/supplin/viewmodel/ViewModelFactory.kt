package com.kmlibs.supplin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kmlibs.supplin.InstanceContainer
import com.kmlibs.supplin.InstanceSupplier
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ViewModelFactory(
    private val viewModelClass: KClass<out ViewModel>,
    private val instanceContainer: InstanceContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(viewModelClass.java)) { EXCEPTION_VIEWMODEL_NOT_FOUND }

        val primaryConstructor = viewModelClass.primaryConstructor
        requireNotNull(primaryConstructor) { EXCEPTION_NO_PRIMARY_CONSTRUCTOR.format(viewModelClass.simpleName) }

        val primaryConstructorParameters = primaryConstructor.parameters
        val instance =
            primaryConstructor.callBy(
                primaryConstructorParameters.associateWith { parameter ->
                    instanceContainer.instanceOf(parameter)
                },
            )

        InstanceSupplier.injectFields(modelClass, instance)

        return instance as T
    }



    companion object {
        private const val EXCEPTION_VIEWMODEL_NOT_FOUND = "ViewModel class not found"
        private const val EXCEPTION_NO_PRIMARY_CONSTRUCTOR = "No primary constructor found for %s"
    }
}


