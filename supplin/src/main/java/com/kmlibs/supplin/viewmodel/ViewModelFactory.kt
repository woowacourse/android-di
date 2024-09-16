package com.kmlibs.supplin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kmlibs.supplin.InstanceContainer
import com.kmlibs.supplin.annotations.Supply
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

class ViewModelFactory(
    private val viewModelClass: KClass<out ViewModel>,
    private val instanceContainer: InstanceContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(viewModelClass.java)) {
            EXCEPTION_VIEWMODEL_NOT_FOUND
        }
        val targetConstructor = targetConstructor()
        val constructorParameters = targetConstructor.parameters
        val instance = instanceOf(targetConstructor, constructorParameters)
        instanceContainer.injectFields(modelClass.kotlin, instance as T)

        return instance
    }

    private fun instanceOf(
        targetConstructor: KFunction<ViewModel>,
        constructorParameters: List<KParameter>
    ): ViewModel =
        targetConstructor.callBy(
            constructorParameters.associateWith { parameter ->
                instanceContainer.instanceOf(parameter)
            },
        )

    private fun targetConstructor(): KFunction<ViewModel> =
        viewModelClass.constructors.firstOrNull { constructor ->
            constructor.hasAnnotation<Supply>()
        } ?: viewModelClass.primaryConstructor.also { constructor ->
            check(constructor?.parameters?.isEmpty() == true)
        } ?: error(EXCEPTION_NO_TARGET_CONSTRUCTOR.format(viewModelClass.simpleName))

    companion object {
        private const val EXCEPTION_VIEWMODEL_NOT_FOUND = "ViewModel class not found"
        private const val EXCEPTION_NO_TARGET_CONSTRUCTOR = "No primary constructor found for %s"
    }
}
