package com.kmlibs.supplin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kmlibs.supplin.Injector
import com.kmlibs.supplin.annotations.SupplinViewModel
import com.kmlibs.supplin.annotations.Supply
import com.kmlibs.supplin.application.ApplicationScopeContainer
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class ViewModelFactory(
    private val viewModelClass: KClass<out ViewModel>,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(viewModelClass.java)) { EXCEPTION_VIEWMODEL_NOT_FOUND }

        val targetConstructor = targetConstructor()
        val constructorParameters = targetConstructor.parameters
        val viewModel = instanceOf(targetConstructor, constructorParameters)

        initializeViewModelModules(viewModel)
        injectFields(viewModel)

        return viewModel as T
    }

    private fun initializeViewModelModules(viewModel: ViewModel) {
        val viewModelAnnotation = viewModelClass.findAnnotation<SupplinViewModel>()
        if (viewModelAnnotation != null) {
            Injector.setModules {
                viewModelModule(viewModel = viewModel, modules = viewModelAnnotation.modules)
            }
        }
    }

    private fun injectFields(viewModel: ViewModel) {
        viewModelClass.memberProperties.filter { it.hasAnnotation<Supply>() }
            .forEach { property ->
                injectSingleField(property, viewModel)
            }
    }

    private fun injectSingleField(
        property: KProperty1<out ViewModel, *>,
        viewModel: ViewModel,
    ) {
        try {
            Injector.componentContainers[viewModelClass]?.injectSingleField(
                property,
                viewModel,
            ) ?: throw IllegalArgumentException()
        } catch (e: IllegalArgumentException) {
            ApplicationScopeContainer.container.injectSingleField(
                property,
                viewModel,
            )
        }
    }

    private fun instanceOf(
        targetConstructor: KFunction<ViewModel>,
        constructorParameters: List<KParameter>,
    ): ViewModel {
        val args: Map<KParameter, Any> =
            constructorParameters.associateWith { parameter ->
                Injector.componentContainers[viewModelClass]?.instanceOf(parameter)
                    ?: ApplicationScopeContainer.container.instanceOf(parameter)
            }

        return targetConstructor.callBy(args)
    }

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
