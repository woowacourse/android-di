package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.FieldInject
import woowacourse.shopping.di.annotation.ParamInject
import woowacourse.shopping.di.container.AnnotationQualifier
import woowacourse.shopping.di.container.DependencyContainer
import woowacourse.shopping.di.container.DependencyInstance
import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object DependencyInjector {
    private const val ERROR_DEPENDENCY_NOT_FOUND =
        "Couldn't find dependency from Dependency Container :"

    lateinit var dependencyContainer: DependencyContainer
        private set

    fun initDependencyContainer(container: DependencyContainer) {
        dependencyContainer = container
    }

    fun <T : Any> createInstance(
        modelClass: KClass<T>,
        qualifier: AnnotationQualifier = null
    ): T {
        val targetInstance: T = checkClassTypeThenGetInstance(modelClass, qualifier)
        setDependencyOfProperties(modelClass, targetInstance)
        dependencyContainer.setInstance(modelClass, targetInstance, qualifier)
        return targetInstance
    }

    private fun <T : Any> checkClassTypeThenGetInstance(
        modelClass: KClass<T>,
        qualifier: AnnotationQualifier,
    ): T {
        val objectInstance: T? = modelClass.objectInstance
        val primaryConstructor: KFunction<T>? = modelClass.primaryConstructor
        return if (primaryConstructor == null) {
            getInstanceFromDependencyContainer(modelClass, qualifier)
        } else if (objectInstance != null) {
            objectInstance
        } else {
            primaryConstructor.isAccessible = true
            getInstanceFromConstructor(primaryConstructor)
        }
    }

    private fun <T : Any> getInstanceFromDependencyContainer(
        modelClass: KClass<T>,
        qualifier: AnnotationQualifier,
    ): T {
        val instanceFromContainer = dependencyContainer.getInstance<T>(modelClass, qualifier)
        return if (instanceFromContainer != null) {
            instanceFromContainer
        } else {
            val implementKClass =
                requireNotNull(dependencyContainer.getImplementation<T>(modelClass, qualifier)) {
                    "$ERROR_DEPENDENCY_NOT_FOUND $modelClass to $qualifier"
                }
            createInstance(implementKClass, qualifier)
        }
    }

    private fun <T : Any> getInstanceFromConstructor(primaryConstructor: KFunction<T>): T {
        val constructorParams = getDependencyOfParameters(primaryConstructor.parameters)
        return primaryConstructor.call(*constructorParams)
    }

    private fun getDependencyOfParameters(parameters: List<KParameter>): Array<Any> {
        return parameters.map { parameter ->
            val classifier = parameter.type.classifier as KClass<*>
            if (parameter.hasAnnotation<ParamInject>()) {
                createInstance(classifier)
            }
        }.toTypedArray()
    }

    private fun <T : Any> setDependencyOfProperties(
        modelClass: KClass<T>,
        targetInstance: T,
    ) {
        modelClass.declaredMemberProperties.forEach { property ->
            if (checkNeedsDependencyInject(property)) {
                property.isAccessible = true
                val classifier = property.returnType.classifier as KClass<*>
                val qualifier: Annotation? =
                    property.annotations.find { it.annotationClass.hasAnnotation<Qualifier>() }
                val dependency: DependencyInstance =
                    createInstance(classifier, qualifier?.annotationClass)
                (property as KMutableProperty<*>).setter.call(targetInstance, dependency)
            }
        }
    }

    private fun checkNeedsDependencyInject(kProperty: KProperty<*>): Boolean =
        kProperty.hasAnnotation<FieldInject>() && kProperty.isLateinit && kProperty is KMutableProperty
}
