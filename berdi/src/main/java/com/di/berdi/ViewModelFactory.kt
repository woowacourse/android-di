package com.di.berdi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.di.berdi.annotation.Inject
import com.di.berdi.util.hasQualifier
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

class ViewModelFactory(private val container: Container) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = requireNotNull(modelClass.kotlin.primaryConstructor)
        val params = mapModulesInConstructor(constructor)
        return constructor.call(*params.toTypedArray()).apply { injectProperties(this) }
    }

    private fun mapModulesInConstructor(constructor: KFunction<Any>): List<Any?> {
        return constructor.parameters.map { param ->
            val annotation =
                param.annotations.firstOrNull { it.hasQualifier() }
            requireNotNull(container.getInstance(param.type.jvmErasure, annotation)) {
                "No matching same type in param | type : ${param.type}"
            }
        }
    }

    private fun injectProperties(viewModel: ViewModel) {
        val properties = viewModel::class.declaredMemberProperties.filter {
            it.hasAnnotation<Inject>()
        }
        properties.forEach { injectProperty(it, viewModel) }
    }

    private fun injectProperty(property: KProperty1<out ViewModel, *>, viewModel: ViewModel) {
        val annotation =
            property.annotations.firstOrNull { it.hasQualifier() }
        val instance = container.getInstance(property.returnType.jvmErasure, annotation)
        property.javaField?.apply {
            isAccessible = true
            set(viewModel, instance)
        }
    }
}
