package com.kmlibs.supplin

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import com.kmlibs.supplin.activity.ActivityLifecycleCallback
import com.kmlibs.supplin.activity.ActivityScopeContainer
import com.kmlibs.supplin.annotations.Module
import com.kmlibs.supplin.annotations.Within
import com.kmlibs.supplin.application.ApplicationScopeContainer
import com.kmlibs.supplin.base.ComponentContainer
import com.kmlibs.supplin.model.Scope
import com.kmlibs.supplin.viewmodel.ViewModelScopeContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class InjectionBuilder {
    private var componentContainers: MutableMap<KClass<*>, ComponentContainer> = mutableMapOf()

    fun applicationModule(
        application: Application,
        vararg modules: KClass<*>,
    ) {
        require(componentContainers[application::class] == null) {
            EXCEPTION_MODULE_ALREADY_INITIALIZED.format(application::class.simpleName)
        }
        modules.forEach { module ->
            requireModuleAnnotation(module)
            requireWithinAnnotation(module, Scope.Application)
        }
        addContainer(
            application::class,
            ApplicationScopeContainer.initializeModules(application.applicationContext, *modules),
        )
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallback())
    }

    fun activityModule(
        activity: ComponentActivity,
        vararg modules: KClass<*>,
    ) {
        require(componentContainers[activity::class] == null) {
            EXCEPTION_MODULE_ALREADY_INITIALIZED.format(activity::class.simpleName)
        }
        modules.forEach { module ->
            requireModuleAnnotation(module)
            requireWithinAnnotation(module, Scope.Activity)
        }
        addContainer(activity::class, ActivityScopeContainer.containerOf(activity, *modules))
    }

    fun viewModelModule(
        viewModel: ViewModel,
        vararg modules: KClass<*>,
    ) {
        require(componentContainers[viewModel::class] == null) {
            EXCEPTION_MODULE_ALREADY_INITIALIZED.format(viewModel::class.simpleName)
        }
        modules.forEach { module ->
            requireModuleAnnotation(module)
            requireWithinAnnotation(module, Scope.ViewModel)
            addContainer(viewModel::class, ViewModelScopeContainer.containerOf(viewModel, module))
        }
    }

    fun removeModuleByComponent(component: KClass<*>) {
        componentContainers.remove(component)
    }

    fun removeAllModules() {
        componentContainers.clear()
    }

    fun build(): Map<KClass<*>, ComponentContainer> = componentContainers.toMap()

    private fun requireModuleAnnotation(module: KClass<*>) {
        require(module.hasAnnotation<Module>()) {
            EXCEPTION_MODULE_ANNOTATION_DOES_NOT_EXIST.format(module.simpleName)
        }
    }

    private fun requireWithinAnnotation(
        module: KClass<*>,
        scope: Scope,
    ) {
        val moduleScope = module.findAnnotation<Within>()?.scope
        require(moduleScope != null && moduleScope == scope::class) {
            EXCEPTION_INVALID_WITHIN_MODULE.format(module.simpleName, scope::class.simpleName)
        }
    }

    private fun addContainer(
        clazz: KClass<*>,
        componentContainer: ComponentContainer,
    ) {
        componentContainers[clazz] = componentContainer
    }

    companion object {
        private const val EXCEPTION_MODULE_ANNOTATION_DOES_NOT_EXIST =
            "Module objects %s should be annotated with @Module."
        private const val EXCEPTION_INVALID_WITHIN_MODULE =
            "Module %s should be annotated with @Within(%s)."
        private const val EXCEPTION_MODULE_ALREADY_INITIALIZED =
            "Module for %s has already been initialized."
    }
}
