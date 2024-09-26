package com.example.sh1mj1.component.viewmodelscope

import androidx.lifecycle.ViewModel
import com.example.sh1mj1.annotation.Inject
import com.example.sh1mj1.annotation.ViewModelScope
import com.example.sh1mj1.component.singleton.ComponentKey
import com.example.sh1mj1.extension.withQualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation

// instance 와 그 인스턴스 스코프 컴포넌트들의 키.
data class ViewModelScopedInstanceWithKeys<VM : ViewModel>(
    val instance: VM,
    val instanceScopeComponentsKeys: List<ComponentKey>,
)

fun KFunction<ViewModel>.annotatedWithInject(): List<KParameter> = this.parameters.filter { kParameter ->
    kParameter.hasAnnotation<Inject>()
}

fun List<KParameter>.viewModelScopeParameterKeys(): List<ComponentKey> = this.map { kParameter ->
    ComponentKey.of(
        clazz = kParameter.type.classifier as KClass<*>,
        qualifier = kParameter.withQualifier(),
    )
}

fun <VM : Any> List<KProperty1<VM, *>>.viewModelScopePropertyKeys(): List<ComponentKey> =
    this.filter { it.hasAnnotation<ViewModelScope>() }
        .map { kProperty ->
            ComponentKey.of(
                clazz = kProperty.returnType.classifier as KClass<*>,
                qualifier = kProperty.withQualifier(),
            )
        }
