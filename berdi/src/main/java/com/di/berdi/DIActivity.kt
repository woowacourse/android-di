package com.di.berdi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.di.berdi.annotation.Inject
import com.di.berdi.util.qualifiedName
import java.lang.reflect.Field
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.javaField

abstract class DIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val properties = this::class.declaredMemberProperties

        val propertiesToInject = properties.filterInjectsProperties()
        setInjectFieldInstance(propertiesToInject)

        val viewModelField = properties.findViewModelField()
        viewModelField?.let { setViewModelInstance(it) }

        super.onCreate(savedInstanceState)
    }

    private fun Collection<KProperty<*>>.findViewModelField(): Field? {
        return firstOrNull {
            val fieldType = requireNotNull(it.javaField?.type)
            ViewModel::class.java.isAssignableFrom(fieldType)
        }?.javaField
    }

    private fun Collection<KProperty<*>>.filterInjectsProperties(): List<KProperty<*>> {
        return filter { it.hasAnnotation<Inject>() }
    }

    private fun setViewModelInstance(viewModelField: Field) {
        viewModelField.setInstance(getNewViewModelByType(viewModelField))
    }

    private fun setInjectFieldInstance(fieldsToInject: List<KProperty<*>>) {
        val application = application as DIApplication
        fieldsToInject.forEach { property ->
            injectField(application, property)
        }
    }

    private fun injectField(application: DIApplication, property: KProperty<*>) {
        val instance = application.injector.getInstanceOf(
            context = this,
            type = property.returnType,
            qualifiedName = property.qualifiedName,
        )
        property.javaField?.setInstance(instance)
    }

    private fun Field.setInstance(instance: Any) {
        isAccessible = true
        set(this@DIActivity, instance)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getNewViewModelByType(field: Field): ViewModel {
        val vm = field.type as? Class<ViewModel>
        requireNotNull(vm) { "No matching ViewModel type | type : ${field.type}" }
        return getViewModel(vm)
    }

    private inline fun <reified VM : ViewModel> getViewModel(type: Class<VM>): VM {
        val application = application as DIApplication
        return ViewModelProvider(
            owner = this@DIActivity,
            factory = ViewModelFactory(this, application.injector),
        )[type]
    }
}
