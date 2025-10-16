package woowacourse.di

import woowacourse.di.annotation.ActivityScope
import woowacourse.di.annotation.Inject
import woowacourse.di.annotation.Qualifier
import woowacourse.di.annotation.Scope
import woowacourse.di.annotation.SingletonScope
import woowacourse.di.annotation.ViewModelScope
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object DIFactory {
    fun <T : Any> create(kClass: KClass<T>): T {
        val instance = createWithConstructor(kClass)
        injectFields(instance)
        return instance
    }

    private fun <T : Any> createWithConstructor(kClass: KClass<T>): T {
        val constructor = kClass.primaryConstructor ?: return kClass.createInstance()
        constructor.isAccessible = true

        val args =
            constructor.parameters.associateWith { param ->
                if (param.hasAnnotation<Inject>()) {
                    val qualifier =
                        param.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }?.annotationClass
                    val scopeAnnotation =
                        param.annotations.firstOrNull { it.annotationClass.hasAnnotation<Scope>() }?.annotationClass
                    val scope = extractScopeFromAnnotations(scopeAnnotation)
                    val typeKClass =
                        param.type.classifier as? KClass<*>
                            ?: throw IllegalArgumentException("Unsupported parameter type: ${param.type}")
                    DIContainer.get(typeKClass, qualifier, scope)
                } else {
                    null
                }
            }

        return constructor.callBy(args.filterValues { it != null })
    }

    private fun <T : Any> injectFields(instance: T) {
        val kClass = instance::class
        kClass.memberProperties
            .filterIsInstance<KMutableProperty1<T, Any?>>()
            .filter { it.hasAnnotation<Inject>() }
            .forEach { property ->
                property.isAccessible = true

                val dependencyClass =
                    property.returnType.classifier as? KClass<*>
                        ?: throw IllegalArgumentException("Unsupported field type: ${property.returnType}")

                val qualifier =
                    property.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }?.annotationClass
                val scopeAnnotation =
                    property.annotations.firstOrNull { it.annotationClass.hasAnnotation<Scope>() }?.annotationClass
                val scope = extractScopeFromAnnotations(scopeAnnotation)

                val dependency = DIContainer.get(dependencyClass, qualifier, scope)
                property.set(instance, dependency)
            }
    }

    private fun extractScopeFromAnnotations(scopeAnnotation: KClass<out Annotation>?): ScopeType {
        return when (scopeAnnotation) {
            SingletonScope::class -> ScopeType.Singleton
            ActivityScope::class -> ScopeType.Activity
            ViewModelScope::class -> ScopeType.ViewModel
            else -> ScopeType.Singleton
        }
    }
}
