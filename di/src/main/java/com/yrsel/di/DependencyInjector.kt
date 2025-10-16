package com.yrsel.di

import android.util.Log
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
    fun <T : Any> injectConstructor(modelClass: Class<T>): T {
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
                    val provider = DependencyContainer.get(definitionKey)
                    provider.get()
                }

        return constructor.callBy(arguments)
    }

    fun <T : Any> injectFields(instance: T): T {
        val instanceClass: KClass<out T> = instance::class
        Log.d("CN", "instance = ${instanceClass.simpleName}")
        instanceClass.declaredMemberProperties
            .filterIsInstance<KMutableProperty1<T, Any?>>()
            .filter { it.isInjectableProperty() }
            .forEach { field ->
                Log.d("CN", "field = ${field.name}")
                val fieldClass: KClass<*> =
                    field.returnType.classifier as? KClass<*> ?: return@forEach

                val qualifier = field.findMetaAnnotationClass<Qualifier>()

                val key = DefinitionKey(fieldClass, qualifier)
                val provider = DependencyContainer.get(key)

                field.apply {
                    isAccessible = true
                    setter.call(instance, provider.get())
                }
            }

        return instance
    }

    private fun <T : Any> KMutableProperty1<T, Any?>.isInjectableProperty() = this.isLateinit && this.hasAnnotation<Inject>()
}
