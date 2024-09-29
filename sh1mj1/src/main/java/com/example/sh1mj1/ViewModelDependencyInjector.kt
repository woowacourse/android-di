package com.example.sh1mj1

import androidx.lifecycle.ViewModel
import com.example.sh1mj1.annotation.Inject
import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.singleton.ComponentKey
import com.example.sh1mj1.component.viewmodelscope.ViewModelScopedInstanceWithKeys
import com.example.sh1mj1.component.viewmodelscope.annotatedWithInject
import com.example.sh1mj1.component.viewmodelscope.viewModelScopeParameterKeys
import com.example.sh1mj1.component.viewmodelscope.viewModelScopePropertyKeys
import com.example.sh1mj1.container.AppContainer
import com.example.sh1mj1.container.viewmodelscope.ViewModelComponentContainer
import com.example.sh1mj1.extension.withQualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class ViewModelDependencyInjector(
    private val appContainer: AppContainer,
) {
    // TODO: nunu createInstance가 많은 역할을 하고 있는 것 같아요! 메서드를 분리해봐도 좋을 것 같습니다.
    fun <VM : ViewModel> viewModelScopedInstanceWithKeys(modelClass: Class<VM>): ViewModelScopedInstanceWithKeys<VM> {
        val kClass = modelClass.kotlin

        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("ViewModel must have a primary constructor: ${kClass.simpleName}")

        val injectedArgs = constructor.annotatedWithInject()

        val instance = calledConstructor(injectedArgs, constructor)
        val injectedFields: List<KProperty1<VM, *>> = kClass.memberProperties.filter { it.hasAnnotation<Inject>() }
        setField(injectedFields, instance)


        val componentConstructorParam = injectedArgs.viewModelScopeParameterKeys()
        val componentFields: List<ComponentKey> = injectedFields.viewModelScopePropertyKeys()

        val viewModelScopeComponents = componentConstructorParam + componentFields

        return ViewModelScopedInstanceWithKeys(
            instance = instance,
            instanceScopeComponentsKeys = viewModelScopeComponents,
        )
    }

    private fun <VM : Any> calledConstructor(
        injectedArgs: List<KParameter>,
        constructor: KFunction<VM>,
    ): VM {
        val constructorArgs =
            injectedArgs.map { kParameter ->
                val componentKey =
                    ComponentKey.of(
                        clazz = kParameter.type.classifier as KClass<*>,
                        qualifier = kParameter.withQualifier(),
                    )
                foundDependency(componentKey)
            }.toTypedArray()

        val viewModel = constructor.call(*constructorArgs)
        return viewModel
    }

    private fun foundDependency(componentKey: ComponentKey): Any = appContainer.find(componentKey)

    private fun <VM : ViewModel> setField(
        injectedFields: List<KProperty1<VM, *>>,
        viewModel: VM,
    ) {
        injectedFields.forEach { field ->
            field.isAccessible = true

            val componentKey =
                ComponentKey.of(
                    clazz = field.returnType.classifier as KClass<*>,
                    qualifier = field.withQualifier(),
                )

            val dependency = foundDependency(componentKey)
            val kMutableProperty =
                field as? KMutableProperty<*>
                    ?: throw IllegalArgumentException("Field must be mutable but not: ${field.name}")
            kMutableProperty.setter.call(viewModel, dependency)
        }
    }

    fun removeViewModelScopeComponent(
        kClass: KClass<*>,
        qualifier: Qualifier?,
    ) {
        ViewModelComponentContainer.instance().remove(kClass, qualifier)
    }
}
