package com.kmlibs.supplin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kmlibs.supplin.application.ApplicationScopeContainer
import com.kmlibs.supplin.Injector
import com.kmlibs.supplin.annotations.Supply
import com.kmlibs.supplin.annotations.Within
import com.kmlibs.supplin.model.Scope
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class ViewModelFactory(
    private val viewModelClass: KClass<out ViewModel>,
    private val modules: List<KClass<*>>,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(viewModelClass.java)) {
            EXCEPTION_VIEWMODEL_NOT_FOUND
        }
        val targetConstructor = targetConstructor()
        val constructorParameters = targetConstructor.parameters
        val instance = instanceOf(targetConstructor, constructorParameters)
        Injector.init {
            viewModelModule(instance, modules = modules.toTypedArray())
        }
        modelClass.kotlin.memberProperties.filter { it.hasAnnotation<Supply>() }.forEach {
            val scope = requireNotNull(it.findAnnotation<Within>()?.scope)
            if (scope == Scope.Application::class) {
                ApplicationScopeContainer.container.injectSingleField(it, instance as T)
            }
            if (scope == Scope.ViewModel::class) {
                ViewModelScopeContainer.containerOf(instance, modules.first())
                    .injectSingleField(it, instance as T)
            }
        }

        return instance as T
    }

    private fun instanceOf(
        targetConstructor: KFunction<ViewModel>,
        constructorParameters: List<KParameter>,
    ): ViewModel {
        return targetConstructor.call()
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
