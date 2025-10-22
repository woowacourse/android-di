package com.yrsel.di

import com.yrsel.di.annotation.Inject
import com.yrsel.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

private const val ERROR_NO_PRIMARY_CONSTRUCTOR = "대상의 주 생성자가 존재하지 않습니다. class = %s"
private const val ERROR_CANNOT_RESOLVE_PARAMETER_TYPE = "대상을 Class 로 변환할 수 없습니다. parameter = %s"

object DependencyInjector {
    fun <T : Any> injectConstructor(
        modelClass: Class<T>,
        identifier: Any? = null,
    ): T {
        val constructor =
            modelClass.kotlin.primaryConstructor ?: error(ERROR_NO_PRIMARY_CONSTRUCTOR)

        val arguments =
            constructor.parameters
                .associateWith { parameter: KParameter ->
                    val parameterClass: KClass<*> =
                        parameter.type.classifier as? KClass<*> ?: error(
                            ERROR_CANNOT_RESOLVE_PARAMETER_TYPE,
                        )
                    val qualifier = parameter.findMetaAnnotationClass<Qualifier>()

                    val definitionKey = DefinitionKey(parameterClass, qualifier)
                    val scope =
                        when (val scopeType = DependencyContainer.get(definitionKey).scope) {
                            is ScopeType.Activity ->
                                identifier?.let { ScopeType.Activity(it) } ?: scopeType

                            is ScopeType.ViewModel ->
                                identifier?.let { ScopeType.ViewModel(it.toString()) } ?: scopeType

                            else -> scopeType
                        }
//                    Log.d(
//                        "CN",
//                        "injectConstructor: parameter = ${parameter.name}, scope =$scope ",
//                    )

                    ScopeContainer.getOrCreate(definitionKey, scope) as T
                }

        return constructor.callBy(arguments)
    }

    fun <T : Any> injectFields(
        instance: T,
        identifier: Any? = null,
    ): T {
        val instanceClass: KClass<out T> = instance::class
        instanceClass.declaredMemberProperties
            .filterIsInstance<KMutableProperty1<T, Any?>>()
            .filter { it.isInjectableProperty() }
            .forEach { field ->
                val fieldClass: KClass<*> =
                    field.returnType.classifier as? KClass<*> ?: return@forEach

                val qualifier = field.findMetaAnnotationClass<Qualifier>()

                val key = DefinitionKey(fieldClass, qualifier)
                val scope =
                    when (val scopeType = DependencyContainer.get(key).scope) {
                        is ScopeType.Activity ->
                            identifier?.let { ScopeType.Activity(it) } ?: scopeType

                        is ScopeType.ViewModel ->
                            identifier?.let { ScopeType.ViewModel(it.toString()) } ?: scopeType

                        else -> scopeType
                    }
//                Log.d(
//                    "CN",
//                    "injectFields: fieldClass = ${fieldClass.simpleName}, scope =$scope ",
//                )
                field.apply {
                    isAccessible = true
                    setter.call(instance, ScopeContainer.getOrCreate(key, scope) as T)
                }
            }

        return instance
    }

    private fun <T : Any> KMutableProperty1<T, Any?>.isInjectableProperty() = this.isLateinit && this.hasAnnotation<Inject>()
}
