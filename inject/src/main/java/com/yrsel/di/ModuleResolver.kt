package com.yrsel.di

import com.yrsel.di.annotation.Provides
import com.yrsel.di.annotation.Qualifier
import com.yrsel.di.annotation.Singleton
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation

private const val ERROR_CAN_NOT_CONVERT_FUNCTION = "함수의 반환 타입을 KClass로 변환할 수 없습니다. KFunction = %s"
private const val ERROR_CAN_NOT_CONVERT_PARAMETER = "파라미터를 KClass로 변환할 수 없습니다. KParameter = %s"

object ModuleResolver {
    internal fun <T : Any> resolve(module: Module): Map<DefinitionKey, Provider<T>> =
        buildMap {
            val kClass: KClass<*> = module::class
            kClass.declaredFunctions
                .filter { it.hasAnnotation<Provides>() }
                .forEach { function: KFunction<*> ->
                    val key: DefinitionKey = function.createDefinitionKey()
                    val provider: Provider<T> = function.createProvider(module)
                    put(key, provider)
                }
        }

    private fun KFunction<*>.createDefinitionKey(): DefinitionKey {
        val definitionClass: KClass<*> =
            this.returnType.classifier as? KClass<*>
                ?: error(ERROR_CAN_NOT_CONVERT_FUNCTION.format(this.name))
        val qualifier = this.findMetaAnnotationClass<Qualifier>()
        return DefinitionKey(definitionClass, qualifier)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> KFunction<*>.createProvider(module: Module): Provider<T> =
        if (hasAnnotation<Singleton>()) {
            SingletonProvider { resolveParameters(module) as T }
        } else {
            FactoryProvider { resolveParameters(module) as T }
        }

    private fun KFunction<*>.resolveParameters(module: Module): Any? {
        val arguments = mutableMapOf<KParameter, Any?>()
        this.parameters.forEach { parameter: KParameter ->
            when (parameter.kind) {
                KParameter.Kind.INSTANCE -> arguments[parameter] = module
                KParameter.Kind.VALUE -> arguments[parameter] = parameter.resolve()
                else -> {}
            }
        }
        return this.callBy(arguments)
    }

    private fun KParameter.resolve(): Any {
        val parameterClass: KClass<*> =
            this.type.classifier as? KClass<*>
                ?: error(ERROR_CAN_NOT_CONVERT_PARAMETER.format(this.name))
        val qualifier = this.findMetaAnnotationClass<Qualifier>()
        val key = DefinitionKey(parameterClass, qualifier)
        val provider = DependencyContainer.get(key)
        return provider.get()
    }
}

inline fun <reified T : Annotation> KAnnotatedElement.findMetaAnnotationClass(): KClass<out Annotation>? =
    annotations.firstOrNull { it.annotationClass.hasAnnotation<T>() }?.annotationClass
