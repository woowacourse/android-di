package com.kmlibs.supplin

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import com.kmlibs.supplin.activity.ActivityScopeContainer
import com.kmlibs.supplin.annotations.Module
import com.kmlibs.supplin.application.ApplicationScopeContainer
import com.kmlibs.supplin.base.ComponentContainer
import com.kmlibs.supplin.viewmodel.ViewModelScopeContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

class InjectionBuilder {
    private var componentContainers: MutableMap<KClass<*>, ComponentContainer> = mutableMapOf()

    fun applicationModule(application: Application, vararg modules: KClass<*>) {
        modules.forEach {
            requireModuleAnnotation(it)
            addContainer(application::class, ApplicationScopeContainer.containerOf(application.applicationContext, it))
        }
    }

    fun activityModule(activity: ComponentActivity, vararg modules: KClass<*>) {
        modules.forEach {
            requireModuleAnnotation(it)
            addContainer(activity::class, ActivityScopeContainer.containerOf(activity, it))
        }
    }

    fun viewModelModule(viewModel: ViewModel, vararg modules: KClass<*>) {
        modules.forEach {
            requireModuleAnnotation(it)
            addContainer(viewModel::class, ViewModelScopeContainer.containerOf(viewModel, it))
        }
    }

    private fun requireModuleAnnotation(module: KClass<*>) {
        require(module.hasAnnotation<Module>()) {
            EXCEPTION_MODULE_ANNOTATION_DOES_NOT_EXIST.format(module.simpleName)
        }
    }

    private fun addContainer(clazz: KClass<*>, componentContainer: ComponentContainer) {
        componentContainers[clazz] = componentContainer
    }

    fun build(): MutableMap<KClass<*>, ComponentContainer> = componentContainers

    companion object {
        private const val EXCEPTION_MODULE_ANNOTATION_DOES_NOT_EXIST =
            "Module objects %s should be annotated with @Module."
    }
}
